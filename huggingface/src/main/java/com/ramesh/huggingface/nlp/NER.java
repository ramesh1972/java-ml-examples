package com.ramesh.huggingface.nlp;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.huggingface.translator.TokenClassificationTranslatorFactory;
import ai.djl.inference.Predictor;
import ai.djl.modality.nlp.translator.NamedEntity;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.djl.util.JsonUtils;
import ai.djl.repository.zoo.ModelZoo;

import java.io.IOException;

public class NER {

	private NER() {
	}

	public static void main(String[] args) throws IOException, ModelException, TranslateException {
        String text = "My name is Wolfgang and I live in Berlin.";

        Criteria<String, NamedEntity[]> criteria =
                Criteria.builder()
                .optApplication(Application.NLP.TEXT_CLASSIFICATION)
                        .setTypes(String.class, NamedEntity[].class)
                        .optModelUrls(
                                "djl://ai.djl.huggingface.pytorch/ml6team/bert-base-uncased-city-country-ner")
                        .optEngine("PyTorch")
                        .optTranslatorFactory(new TokenClassificationTranslatorFactory())
                        .optProgress(new ProgressBar())
                        .build();

        try (ZooModel<String, NamedEntity[]> model = ModelZoo.loadModel(criteria);
                Predictor<String, NamedEntity[]> predictor = model.newPredictor()) {
        	NamedEntity[] res = predictor.predict(text);
            System.out.println(JsonUtils.GSON_PRETTY.toJson(res));
        }
    }
}