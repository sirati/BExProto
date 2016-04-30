package de.sirati97.bex_proto.events.gen;

import de.sirati97.bex_proto.events.Event;
import de.sirati97.bex_proto.events.Listener;
import gnu.trove.map.hash.THashMap;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import static org.apache.bcel.Constants.*;

/**
 * Created by sirati97 on 30.04.2016.
 */
public class ClassBuilder implements Listener {
    public final static ClassBuilder INSTANCE = new ClassBuilder();
    public static boolean generateClasses = true;
    public static boolean allowNonPublic = false;

    private ClassBuilder(){}

    private int id = 0;
    private Type listenerInterfaceType = Type.getType(Listener.class);
    private Type eventInterfaceType = Type.getType(Event.class);
    private Type methodType = Type.getType(Method.class);
    private final BuilderClassLoader classLoader = new BuilderClassLoader();
    private final Map<Method, MethodCaller> callerMap = new THashMap<>();


    public MethodCaller getEventCaller(Method method) {
        boolean isPublic = Modifier.isPublic(method.getModifiers());
        if (isPublic && generateClasses) {
            MethodCaller caller = callerMap.get(method);
            if (caller == null) {
                try {
                    caller = buildEventCaller(method);
                } catch (IOException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new IllegalStateException("Could not build MethodCaller: ", e);
                }
                callerMap.put(method, caller);
            }
            return caller;
        } else {
            if (!allowNonPublic && !isPublic) {
                throw new IllegalStateException("Method " + method.getName() + " has to be public. You can allow non public event handlers by setting ClassBuilder.allowNonPublic to true");
            }
            return ReflectionMethodCaller.INSTANCE;
        }
    }

    private MethodCaller buildEventCaller(Method method) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className = "de.sirati97.bex_proto.events.gen.runtime.MethodCaller_RuntimeGenImpl_"+id++;
        Type eventType = Type.getType(method.getParameterTypes()[0]);
        Type listenerType = Type.getType(method.getDeclaringClass());

        ClassGen classGen = new ClassGen(className,"java.lang.Object","<generated>", ACC_PUBLIC | ACC_SUPER, new String[]{"de.sirati97.bex_proto.events.gen.MethodCaller"});
        ConstantPoolGen constantPool = classGen.getConstantPool();
        InstructionList instructions = new InstructionList();
        InstructionFactory factory = new InstructionFactory(classGen);
        instructions.append(new ALOAD(2)); //0 = this, 1 = first parameter
        instructions.append(factory.createCast(listenerInterfaceType, listenerType));
        instructions.append(new ALOAD(3));
        instructions.append(factory.createCast(eventInterfaceType, eventType));
        instructions.append(factory.createInvoke(method.getDeclaringClass().getName(), method.getName(), Type.VOID, new Type[]{eventType}, INVOKEVIRTUAL));
        instructions.append(InstructionConstants.RETURN);

        MethodGen methodGen = new MethodGen(ACC_PUBLIC, Type.VOID, new Type[]{methodType, listenerInterfaceType, eventInterfaceType}, new String[]{"method","listener","event"}, "invoke", className, instructions, constantPool);
        methodGen.setMaxStack();
        classGen.addMethod(methodGen.getMethod());
        instructions.dispose();
        classGen.addEmptyConstructor(ACC_PUBLIC);
        //generate class bytecode
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JavaClass javaClass = classGen.getJavaClass();
        javaClass.dump(outputStream);
        Class clazz = classLoader.defineClass(className, outputStream.toByteArray());
        //instance
        Constructor constructor = clazz.getConstructor();
        return (MethodCaller) constructor.newInstance();
    }
}
