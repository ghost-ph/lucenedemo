package com.patterncat.lucene.jrip;

public class TestJRip {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Create numeric attributes "length" and "weight"
        Attribute t1 = new Attribute("比赛");
        Attribute t2 = new Attribute("涨停");
        Attribute t3 = new Attribute("生物");
        Attribute t4 = new Attribute("通讯");

        // Create vector to hold nominal values "first", "second", "third"
        FastVector my_nominal_values = new FastVector(3);
        my_nominal_values.addElement("体育");
        my_nominal_values.addElement("财经");
        my_nominal_values.addElement("科技");

        // Create nominal attribute "position"
        Attribute textClass = new Attribute("类别", my_nominal_values);

        // Create vector of the above attributes
        FastVector attributes = new FastVector(5);
        attributes.addElement(t1);
        attributes.addElement(t2);
        attributes.addElement(t3);
        attributes.addElement(t4);
        attributes.addElement(textClass);

        // Create the empty dataset "race" with above attributes
        Instances race = new Instances("训练库", attributes, 1);

        // Make position the class attribute
        race.setClassIndex(textClass.index());

        // Create empty instance with three attribute values
        Instance inst = new Instance(5);

        // Set instance's values for the attributes "length", "weight", and
        // "position"
        inst.setValue(t1, 5.3);
        inst.setValue(textClass, "体育");

        // Set instance's dataset to be the dataset "race"
        inst.setDataset(race);

        race.add(inst);

        // Print the instance
        System.out.println("The instance: " + inst);

        // Print the first attribute
        System.out.println("First attribute: " + inst.attribute(0));

        // Print the class attribute
        System.out.println("Class attribute: " + inst.classAttribute());

        // Print the class index
        System.out.println("Class index: " + inst.classIndex());

        // Say if class is missing
        System.out.println("Class is missing: " + inst.classIsMissing());

        // Print the instance's class value in internal format
        System.out.println("Class value (internal format): "+ inst.classValue() +" label:"
                + my_nominal_values.elementAt((int)inst.classValue()).toString());

        // Create empty instance with three attribute values
        Instance inst2 = new Instance(5);

        // Set instance's values for the attributes "length", "weight", and
        // "position"
        inst2.setValue(t2, 10);
        inst2.setValue(textClass, "财经");

        // Set instance's dataset to be the dataset "race"
        inst2.setDataset(race);
        race.add(inst2);

        // Create empty instance with three attribute values
        Instance inst3 = new Instance(5);

        // Set instance's values for the attributes "length", "weight", and
        // "position"
        inst3.setValue(t3, 10);
        inst3.setValue(textClass, "科技");

        // Set instance's dataset to be the dataset "race"
        inst3.setDataset(race);
        race.add(inst3);

        for (int i = 0; i < race.numInstances(); i++) {
            race.instance(i).setWeight(1.0);
        }

        System.out.println(race);

        System.out.println(race.sumOfWeights());
        JRip rip = new JRip();
        rip.buildClassifier(race);
        double[] result = rip.distributionForInstance(inst);
        for(int i=0;i<result.length;++i)
        {
            System.out.println("result: " + result[i]);
        }
        System.out.println(rip.toString());
    }
}
