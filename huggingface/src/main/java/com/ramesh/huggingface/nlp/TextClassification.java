package com.ramesh.huggingface.nlp;

import ai.djl.ModelException;
import ai.djl.huggingface.translator.TextClassificationTranslatorFactory;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import java.io.IOException;

public class TextClassification {

    private TextClassification() {}

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String text = "DJL is the best.";

        Criteria<String, Classifications> criteria =
                Criteria.builder()
                        .setTypes(String.class, Classifications.class)
                        .optModelUrls(
                                "djl://ai.djl.huggingface.pytorch/distilbert-base-uncased-finetuned-sst-2-english")
                        .optEngine("PyTorch")
                        .optTranslatorFactory(new TextClassificationTranslatorFactory())
                        .optProgress(new ProgressBar())
                        .build();

        try (ZooModel<String, Classifications> model = criteria.loadModel();
                Predictor<String, Classifications> predictor = model.newPredictor()) {
            Classifications res = predictor.predict(text);
            System.out.println(res);
        }
    }
}