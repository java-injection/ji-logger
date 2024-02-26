package it.ji.logger.apt;


import it.ji.logger.annotations.ConsoleLogger;
import javassist.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@SupportedAnnotationTypes({
        "it.ji.logger.annotations.LoggerCore",
        "it.ji.logger.annotations.ConsoleLogger",
        "it.ji.logger.annotations.WithJavaInjectionLogger"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class LoggerProcessor extends AbstractProcessor {
    private final List<String> visitedClazz = new ArrayList<>();
    private String consoleLoggerClazz;

    public LoggerProcessor() {
        super();
        System.out.println("[Java-Injection][Logger-APT] APT Processor initiated");
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {

        System.out.println("[Java-Injection][Logger-APT] Processing annotations");
        if(consoleLoggerClazz == null){
            consoleLoggerClazz = findConsoleLoggerClass(env);
        }
        for (TypeElement te : elements) {
            System.out.println("TE -> " + te);
            if (te.getQualifiedName().toString().equals("it.ji.logger.annotations.LoggerCore")) {

                System.out.println("te = " + te.getQualifiedName());
                for (Element e : env.getElementsAnnotatedWith(te)) {
                    System.out.println(">> e = " + e.getSimpleName());
                    System.out.println("Enclosing: " + e.getEnclosingElement().asType().toString());
                    String containerClazz = e.getEnclosingElement().asType().toString() + "." + e.getSimpleName().toString();


                    //========================
//                System.out.println("key -> " + key);
                    final ClassPool pool = ClassPool.getDefault();
                    try {

                        File root = new File("target/classes");
                        ArrayList<URL> urls = new ArrayList<URL>();
                        URLClassLoader parent = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), this.getClass().getClassLoader());

                        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()}, parent);
                        System.out.println("container clazz to load is: " + containerClazz);
                        Class<?> loadClass = classLoader.loadClass(containerClazz);

                        ClassPool.getDefault().insertClassPath(new ClassClassPath(loadClass));

                        final CtClass compiledClass = pool.get(containerClazz);
                        System.out.println("[I18N] loading class: " + containerClazz);

                        System.out.println("[I18N]Instrumentation begin");
                        CtConstructor constructor = compiledClass.getDeclaredConstructors()[0];
                        if (constructor != null) {
//                        this.title = TranslatorManager.getInstance().getTranslation(title);

//                            constructor.insertAfter("System.out.println(\"Value: \"+" + e.getSimpleName() + ");");
                            if (!visitedClazz.contains(containerClazz)) {
                                System.out.println("[I18N] line added before: it.cnr.istc.i18n.translator.TranslatorManager.getInstance().addObjectToTranslate(this);");
                                System.out.println("[I18N][OK] Constructor modified");
                                constructor.insertAfter("System.out.println(\"Sono stato aggiunto dall'APT di LoggerProcessor !!\");");
                                visitedClazz.add(containerClazz);
                            }
                        }

                        compiledClass.stopPruning(true); // don't delete the data if it is written out.
                        compiledClass.writeFile("target/classes");
                        compiledClass.defrost();
                        System.out.println("[I18N] class file written, compiled class defrosted.");
                        System.out.println("[I18N]Instrumentation end");

                    } catch (NotFoundException ex) {
                        System.err.println("A1");
                        ex.printStackTrace();
                        Logger.getLogger(LoggerProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (CannotCompileException ex) {
                        System.err.println("A2");
                        ex.printStackTrace();
                        Logger.getLogger(LoggerProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        System.err.println("A3");
                        ex.printStackTrace();
                        Logger.getLogger(LoggerProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        System.err.println("A3");
                        ex.printStackTrace();
                        Logger.getLogger(LoggerProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        System.err.println("A5");
                        ex.printStackTrace();
                        Logger.getLogger(LoggerProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(LoggerProcessor.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println("EE");
                        ex.printStackTrace();
                    }

                }
            } else if (te.getQualifiedName().toString().equals("it.ji.logger.annotations.WithJavaInjectionLogger") && consoleLoggerClazz != null) {
                System.out.println("te = " + te.getQualifiedName());
                System.out.println("[DEBUG][JI-Logger] consoleLoggerClazz = " + consoleLoggerClazz);

                for (Element e : env.getElementsAnnotatedWith(te)) {
                    System.out.println(">> e = " + e.getSimpleName());
                    System.out.println("Enclosing: " + e.getEnclosingElement().asType().toString());
                    String containerClazz = e.getEnclosingElement().asType().toString() + "." + e.getSimpleName().toString();

//                    // Cast Element to TypeElement to access type-specific methods
//                    TypeElement classElement = (TypeElement) e;
//
//                    // Get a list of interfaces implemented by the class
//                    List<? extends TypeMirror> interfaces = classElement.getInterfaces();
//
//                    // Check if LoggerComponent is one of the implemented interfaces
//                    boolean implementsLoggerComponent = interfaces.stream()
//                            .anyMatch(iface -> iface.toString().equals("it.ji.logger.core.LoggerComponent"));

//                    if (implementsLoggerComponent) {
                        try {
                            final ClassPool pool = ClassPool.getDefault();
                            File root = new File("target/classes");
                            ArrayList<URL> urls = new ArrayList<URL>();
                            URLClassLoader parent = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), this.getClass().getClassLoader());

                            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()}, parent);
                            System.out.println("container clazz to load is: " + containerClazz);
                            Class<?> loadClass = classLoader.loadClass(containerClazz);

                            ClassPool.getDefault().insertClassPath(new ClassClassPath(loadClass));

                            final CtClass compiledClass = pool.get(containerClazz);
                            System.out.println("[DEBUG][JI-Logger]  loading class: " + containerClazz);

                            System.out.println("[DEBUG][JI-Logger] Instrumentation begin");


                            System.out.println("Class " + containerClazz + " implements LoggerComponent");

                            // Creare un nuovo blocco di codice statico
                            CtConstructor classInitializer = compiledClass.makeClassInitializer();
                            System.out.println("[DEBUG][JI-Logger] Adding static block to class " + containerClazz);

//                            classInitializer.insertBefore(
//                                    "it.ji.logger.core.Logger.LoggerBuilder.setConsoleLogger(new " + containerClazz + "()); " +
//                                            "System.out.println(\"LoggerComponent added to Logger\");");

//                            String block = "String loggerBuilderClassName = \"it.ji.logger.core.Logger$LoggerBuilder\";" +
//                                    "String consoleLoggerClassName = \"it.ji.demo.DemoConsoleLogger\";" +
//                                    "String consoleLoggerComponent = \"it.ji.logger.core.LoggerComponent\";" +
//                                    "try {" +
//                                    "    Class loggerBuilderClass = Class.forName(loggerBuilderClassName);" +
//                                    "    Class consoleLoggerClass = Class.forName(consoleLoggerClassName);" +
//                                    "    Class loggerComponentClass = Class.forName(consoleLoggerComponent);" +
//                                    "    java.lang.reflect.Constructor loggerBuilderConstructor = loggerBuilderClass.getConstructor();" +
//                                    "    loggerBuilderConstructor.setAccessible(true);" +
//                                    "    Object loggerBuilderInstance = loggerBuilderConstructor.newInstance();" +
//                                    "    java.lang.reflect.Method setConsoleLoggerMethod = loggerBuilderClass.getDeclaredMethod(\"setConsoleLogger\", loggerComponentClass);" +
//                                    "    setConsoleLoggerMethod.setAccessible(true);" +
//                                    "    setConsoleLoggerMethod.invoke(loggerBuilderInstance, consoleLoggerClass.getConstructor().newInstance());" +
//                                    "} catch (Exception e) {" +
//                                    "    e.printStackTrace();" +
//                                    "}" +
//                                    "System.out.println(\"LoggerComponent added to Logger\");";

                            System.out.println("[DEBUG][JI-Logger] Adding code: it.ji.logger.core.Logger.LoggerBuilder.setConsoleLogger(new " + consoleLoggerClazz + "());");

                            String block = "it.ji.logger.core.Logger.LoggerBuilder.setConsoleLogger(new " + consoleLoggerClazz + "());";

                            System.out.println("[WARNING][JI-Logger] Adding code:\n " + block);

                            classInitializer.insertBefore(block);


                            compiledClass.stopPruning(true); // don't delete the data if it is written out.
                            compiledClass.writeFile("target/classes");
                            compiledClass.defrost();
                            System.out.println("[DEBUG][JI-Logger] class file written, compiled class defrosted.");
                            System.out.println("[DEBUG][JI-Logger] Instrumentation end");
                        } catch (Exception ex) {
                            ex.printStackTrace(
                            );
                        }


//                    } else {
//                        System.out.println("[ERROR] Class " + containerClazz + " does not implement LoggerComponent");
//                    }
                }
            }
        }

        return true;
    }

    private String findConsoleLoggerClass(RoundEnvironment env) {
        System.out.println("[DEBUG][Java-Injection][Logger-APT] Searching for ConsoleLogger class");
        //load class kwnowing the full name that is: it.ji.logger.core.annotations.ConsoleLogger
        for(Element e : env.getElementsAnnotatedWith(ConsoleLogger.class)){
            TypeElement classElement = (TypeElement) e;

            // Get a list of interfaces implemented by the class
            List<? extends TypeMirror> interfaces = classElement.getInterfaces();

            // Check if LoggerComponent is one of the implemented interfaces
            boolean implementsLoggerComponent = interfaces.stream()
                    .anyMatch(iface -> iface.toString().equals("it.ji.logger.core.LoggerComponent"));
            if (implementsLoggerComponent) {
                System.out.println("[DEBUG][Java-Injection][Logger-APT] Found ConsoleLogger class: " + classElement.getQualifiedName().toString());
                return classElement.getQualifiedName().toString();
            }else{
                System.out.println("[ERROR][Java-Injection][Logger-APT] ConsoleLogger class does not implement LoggerComponent");
            }
//            System.out.println("[DEBUG][Java-Injection][Logger-APT] Found ConsoleLogger class: " + e.getEnclosingElement().asType().toString() + "." + e.getSimpleName().toString());
//            return e.getEnclosingElement().asType().toString() + "." + e.getSimpleName().toString();
        }
        System.out.println("[ERROR][Java-Injection][Logger-APT] ConsoleLogger class not found");
        return null;
    }
}
