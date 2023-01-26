package projekt.projekt.Utils;

import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class ReflectionUtils {
    private static boolean isPrivate(int modifers){
        return Modifier.isPrivate(modifers);
    }

    private static boolean isProtected(int modifers){
        return Modifier.isProtected(modifers);
    }

    private static boolean isPublic(int modifers){
        return Modifier.isPublic(modifers);
    }

    private static boolean isStatic(int modifers){
        return Modifier.isStatic(modifers);
    }

    private static boolean isFinal(int modifers){
        return Modifier.isFinal(modifers);
    }

    public static String retrieveModifiers(int modifiers) {
        String modifiersString = "";

        if(isPublic(modifiers)) {
            modifiersString += "public ";
        }

        if(isPrivate(modifiers)) {
            modifiersString += "private ";
        }

        if(isProtected(modifiers)) {
            modifiersString += "protected ";
        }

        if(isStatic(modifiers)) {
            modifiersString += "static ";
        }

        if(isFinal(modifiers)) {
            modifiersString += "final ";
        }

        return modifiersString;
    }

    public static String retreiveParameters(Parameter[] parameters) {

        String paramsString = "";

        for(Parameter p : parameters) {
            paramsString += p.getType().getSimpleName() + " " + p.getName();

            if(!p.equals(parameters[parameters.length - 1])) {
                paramsString += ", ";
            }
        }

        return paramsString;
    }
}
