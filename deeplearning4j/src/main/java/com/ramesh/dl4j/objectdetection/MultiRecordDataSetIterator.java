package com.ramesh.dl4j.objectdetection;

import org.datavec.image.transform.ImageTransform;
import org.nd4j.linalg.dataset.MultiDataSet;
import org.nd4j.linalg.dataset.api.MultiDataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import com.ramesh.dl4j.objectdetection.MulRecordDataLoader;

public class MultiRecordDataSetIterator implements MultiDataSetIterator {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int batchSize;
    private int batchNum = 0;
    private int numExample;
    private MulRecordDataLoader load;
    private MultiDataSetPreProcessor preProcessor;

    public MultiRecordDataSetIterator(int batchSize, String dataSetType) throws Exception {
        this(batchSize, null, dataSetType);
    }
    private MultiRecordDataSetIterator(int batchSize, ImageTransform imageTransform, String dataSetType) throws Exception {
        this.batchSize = batchSize;
        load = new MulRecordDataLoader(imageTransform, dataSetType);
        numExample = load.totalExamples();
    }


    @Override
    public MultiDataSet next(int i) {
        batchNum += i;
        MultiDataSet mds = load.next(i);
        if (preProcessor != null) {
            preProcessor.preProcess(mds);
        }
        return mds;
    }

    @Override
    public void setPreProcessor(MultiDataSetPreProcessor multiDataSetPreProcessor) {
        this.preProcessor = multiDataSetPreProcessor;
    }

    @Override
    public MultiDataSetPreProcessor getPreProcessor() {
        return preProcessor;
    }

    @Override
    public boolean resetSupported() {
        return true;
    }

    @Override
    public boolean asyncSupported() {
        return true;
    }

    @Override
    public void reset() {
        batchNum = 0;
        load.reset();
    }

    @Override
    public boolean hasNext() {
        return batchNum < numExample;
    }

    @Override
    public MultiDataSet next() {
        return next(batchSize);
    }
}
