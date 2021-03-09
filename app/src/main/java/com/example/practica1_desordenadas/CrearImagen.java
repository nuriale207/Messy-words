package com.example.practica1_desordenadas;





import android.content.Context;
import android.content.ContextWrapper;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

import java.util.ArrayList;
import java.util.List;

public class CrearImagen {

    private String letras;
    private ArrayList<String> listaLetras;

    public CrearImagen(String pLetras) {
        this.letras = pLetras;
        dividirString();
    }

    public void generarImagen(Context ctx) {
//        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
//        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(listaLetras);
//        final Dimension dimension = new Dimension(600, 600);
//        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
//        wordCloud.setPadding(2);
//        wordCloud.setBackground(new CircleBackground(300));
//        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
//        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
//        ContextWrapper cw = new ContextWrapper(ctx.getApplicationContext());
//        wordCloud.build(wordFrequencies);
//        wordCloud.writeToFile("kumo-core/output/datarank_wordcloud_circle_sqrt_font.png");
//
}

    private void dividirString() {
        listaLetras=new ArrayList<String>();
        for (int i = 0; i < letras.length(); i++) {
            listaLetras.add(String.valueOf(letras.charAt(i)));
        }

    }


}
