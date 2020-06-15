package service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.IOException;

public class Service {

    public static void main(String[] args) throws IOException {
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        Translation translation = translate.translate("¡Hola Mundo!");
        System.out.printf("Translated Text:\n\t%s\n", translation.getTranslatedText());
    }
}
