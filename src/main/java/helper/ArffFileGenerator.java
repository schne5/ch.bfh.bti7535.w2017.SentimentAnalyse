package helper;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ArffFileGenerator {
    private Path path;
    private  FastVector atts;
    Instances  data;
    double[]   vals;

    public ArffFileGenerator(Path p){
        this.path =p;
        this.atts = new FastVector();
    }
    public  void generateFile(){
        System.out.println(data);
        try {
            Files.write( Paths.get("trainingdata\\"+"trainingData.arff"), data.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addValues(double[] values){
        if(data == null)
            data = new Instances("FilmReview", atts, 0);
        if(values.length > data.numAttributes()){
            System.out.println("More values than attributes");
            return;
        }
        vals = new double[data.numAttributes()];
        for(int i =0; i<vals.length; i++){
            vals[i] = values[i];
        }
        data.add(new Instance(1.0, vals));
    }

    public void addNumericAttribute(String attributeName){
        atts.addElement(new Attribute(attributeName));
    }

    public void addStringAttribute(String attributeName){
        atts.addElement(new Attribute(attributeName, (FastVector) null));
    }

    public void addStringAttribute(String attributeName, List<String> attributeValues){
       FastVector attValues = new FastVector();
       for(String value: attributeValues){
           attValues.addElement(value);
       }
        atts.addElement(new Attribute(attributeName, attValues));
    }
}
