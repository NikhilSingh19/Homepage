import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This class creates and trains an OCR network that uses 26 inputs, 
 * one to recognize each letter
 * 
 * Within the class there are three major parts:
 * 
 * First, the network itself is calculated using the
 * method calcNetwork, there are two versions, calcNetwork() which calculates the
 * entire network, and calcNetwork(int a) that just calculates the network for one model or letter
 * specified by the parameter int a.
 * 
 * Within the calcNetwork method, there are methods which calculate the values of the hidden
 * and output layer. Along with those there are methods to randomize the weights, calculate the
 * value of the activation function and the derivative of the activation function and calculate
 * the error.
 * 
 * Secondly, the network is trained using the calcAndChange method which features
 * four new methods not found in the calculation of the network. The new methods
 * are: calcPsi(int a), calcUpperPsi(int a), changeAH(int a), changeHF(int a).
 * 
 * These methods work in combination with each other to use the backpropagation algorithm
 * to train the network.
 * 
 * @author Nikhil Singh
 * @version January 7, 2015
 */
public class FacialRecognition {
	//i, the number of nodes in the F layer
	final static int i=12;

	//number of nodes in the hidden layer
	final static int j=100;

	//number of nodes in the input layer
	final static int k=4000;

	//arraylist of weights from A to H
	double[][] weightsAH;

	//number of models
	final static int models=12;

	//arraylist of weights from H to F
	double[][] weightsHF;

	//arraylist of the changeToAdd in weights from A to H
	double[][] deltaWeightsAH;

	//arraylist of the inputs
	double[][] inputs=new double[k][models];

	//arraylist of the hidden layer 
	double[][] outputs=new double[i][models];

	//arraylist of the outputs in the training set
	double[][] trainOutputs=new double[i][models];

	//arraylist of the hidden layer 
	double[][] hiddenLayer=new double[j][models];

	//arraylist of the changeToAdd in weights from H to F
	double[][] deltaWeightsHF;

	//the learning constant which provides a control over the steepest descent
	//it lets us take the process in slow steps so we do not overshoot
	double learningConstant=.5;

	//stores the error value for the network
	double error;

	//stores the psi values for each output node
	double [][] psi=new double[i][models];

	//stores the values used to calculate the change in weights between the hidden and output 
	//layers
	double [][] upperPsi=new double[j][models];

	//array of all the letters
	static String[] letters={"A", "B", "C", "D", "E", "F", "G", "H", "I","J","K","L","M","N","O",
		"P","Q","R","S","T","U","V","W","X","Y","Z"};

	//array of all the names for facial recognition
	static String[] names={"Rishabh","Hemant","Ryan","Pranav","Nikhil","Vineet","Nikita","Alex","Jonathan","Anika","Zabin","Nathan"};

	//constant to mark range of the random weights
	final double RANGE=1.5;

	//constant to mark amount the random weights go below 0
	final double BELOW=.75;

	//number that helps to implement an adaptive learning constant
	final static double CHANGE_LEARN=.5;

	//number that dictates what number to randomize weights have to be below before the network starts training
	final static double WEIGHTS_BELOW=16;

	//number that dictates when the weights for the network are low enough to stop training
	final static double STOP_TRAINING=.00001;

	//stores the values of the theta(j) values
	double[][] thetaJ=new double[j][models];

	//stores the values of the theta(i) values
	double[][] thetaI=new double[i][models];


	/**
	 * Initializes a neural network with the given inputs and expected outputs.
	 * @param inputs is a 2d array with the inputs, it is broken down by each training set
	 * @param outputs is a 2d array with the expected outputs, this will be used to train the network
	 */
	public FacialRecognition(double[][] inputs, double[][] outputs)
	{
		this.inputs=inputs;
		this.trainOutputs=outputs;
	}

	/**
	 * Calculates random weights for the network and adds them to the 
	 * array of weights. This is used as a starting point for using steepest descent
	 * and Backpropagation. It initializes the random weights so that from
	 * there we can see which direction to go and start to close in on the minimum.
	 * The weights are initialized for both weightsAH and weightsHF and are given a random
	 * value dictated by RANGE and BELOW.
	 */
	public void randWeights() 
	{
		weightsAH=new double[k][j];
		deltaWeightsAH=new double[k][j];
		for(int a=0;a<k;a++)
		{
			for(int b=0; b<j;b++) 
			{
				weightsAH[a][b]=Math.random()*RANGE-BELOW;
			}
		}
		weightsHF=new double[j][i];
		deltaWeightsHF=new double[j][i];
		for(int a=0;a<j;a++)
		{ 
			for(int b=0; b<i;b++) 
			{
				weightsHF[a][b]=Math.random()*RANGE-BELOW; 
			}
		}
		return;
	}

	/**
	 * This method initializes the arrays for the deltaWeight arrays. This is necessary
	 * so that they can be accessed later.
	 */
	public void initializeDeltas()
	{
		deltaWeightsAH=new double[k][j];
		deltaWeightsHF=new double[j][i];
		return;
	}

	/** Calculates the sigmoid function using x as the input the sigmoid function is 1/(e^x+1).
	 * @param x the input to the sigmoid
	 * @return the result of the sigmoid with x in it
	 */
	public double sigmoid(double x)
	{
		double toReturn=1/(Math.exp(-x)+1);
		return toReturn;
	}

	/** Calculates the derivative of the sigmoid function using x as the input.
	 * @param x the input to the derivative of the sigmoid
	 * @return the result of the derivative of the sigmoid with x in it
	 */
	public double derSigmoid(double x)
	{
		double z=sigmoid(x);
		return z*(1-z);
	}

	/**
	 * Calculates the values in the hidden layer based on inputs and weight.
	 * For each hidden layer node, it multiplies each input by each weight 
	 * relating to the input and the hidden layer node, and
	 * applies the activation function or the sigmoid function. Then the steps
	 * are repeated for each node in the hidden layer.
	 */
	public void calcHiddenLayer()
	{
		for(int c=0;c<models;c++)
		{
			for(int a=0;a<j;a++)
			{
				for( int b=0;b<k;b++)
				{
					hiddenLayer[a][c]+=weightsAH[b][a]*inputs[b][c];
				}
			}
			for(int a=0;a<j;a++)
			{
				hiddenLayer[a][c]=sigmoid(hiddenLayer[a][c]);
			}
		}
		return;
	}

	/**
	 * Calculates the values of the outputs based on the hidden layer For each
	 * output layer node, it multiplies each hidden layer node by the weight
	 * that relates to the hidden layer node and the output layer node. Then for
	 * each output layer node, it adds up all of the values and then applies the
	 * sigmoid function to them, which is the activation function.
	 */
	public void calcoutputsLayer()
	{
		for(int c=0;c<models;c++)
		{
			for(int a=0;a<i;a++)
			{
				for(int b=0;b<j;b++)
				{
					outputs[a][c]+=weightsHF[b][a]*hiddenLayer[b][c];
				}
			}
			for(int a=0;a<i;a++)
			{
				outputs[a][c]=sigmoid(outputs[a][c]);
			}
		}
		return;
	}

	/**
	 * It sets the values of everything in the hidden layer to 0, because
	 * the way I calculate the hidden layer requires that it be cleared every time
	 * since I just keep adding to the hidden layer. So if I don't clear the outputs
	 * they will just keep adding on each other causing a calculation error.
	 */
	public void clearHidden()
	{
		for(int a=0;a<hiddenLayer.length;a++)
		{
			for(int b=0;b<hiddenLayer[0].length;b++)
			{
				hiddenLayer[a][b]=0;
			}
		}
		return;
	}

	/**
	 * It sets the values of everything in the output layer to 0, because
	 * the way I calculate the output layer requires that it be cleared every time
	 * since I just keep adding to the output layer. So if I don't clear the outputs
	 * they will just keep adding to each other causing a calculation error
	 */
	public void clearoutputs()
	{
		for(int a=0;a<outputs.length;a++)
		{
			for(int b=0;b<outputs[0].length;b++)
			{
				outputs[a][b]=0;
			}
		}
		return;
	}

	/**
	 * Uses the previous methods to calculate the network, first it will clear
	 * the hidden and output layers, then calculates them and finally will calculate
	 * the error.
	 */
	public void calcNetwork()
	{
		clearHidden();
		clearoutputs();
		calcHiddenLayer();
		calcoutputsLayer();
		calcError();
		return;
	}

	/**
	 * Uses the previous methods to calculate the network, first it will clear
	 * the hidden and output layers, then calculates them and finally will calculate
	 * the error
	 * 
	 * This version is different. It calculates the network for just one model.
	 * It helps reduce the amount of calculations during the training process
	 */
	public void calcNetwork(int a)
	{
		clearHidden(a);
		clearoutputs(a);
		calcHiddenLayer(a);
		calcoutputsLayer(a);
		return;
	}

	/**
	 * Calculates the values in the hidden layer based on inputs and weight.
	 * For each hidden layer node, it multiplies each input by each weight 
	 * relating to the input and the hidden layer node, and
	 * applies the activation function or the sigmoid function. Then the steps
	 * are repeated for each node in the hidden layer.
	 * 
	 * This version is different. It calculates the hidden layer for just one model.
	 * It helps reduce the amount of calculations during the training process.
	 */
	public void calcHiddenLayer(int c)
	{
		for(int a=0;a<j;a++)
		{
			for( int b=0;b<k;b++)
			{
				hiddenLayer[a][c]+=weightsAH[b][a]*inputs[b][c];
			}
		}
		for(int a=0;a<j;a++)
		{
			hiddenLayer[a][c]=sigmoid(hiddenLayer[a][c]);
		}
		return;
	}

	/**
	 * Calculates the values of the outputs based on the hidden layer.
	 * For each output layer node, it multiplies each hidden layer node by the weight that
	 * relates to the hidden layer node and the output layer node. Then
	 * for each output layer node, it adds up all of the values and
	 * then applies the sigmoid function to them, which is the activation function.
	 * 
	 * This version is different. It calculates the output layer for just one model.
	 * It helps reduce the amount of calculations during the training process
	 */
	public void calcoutputsLayer(int c)
	{
		for(int a=0;a<i;a++)
		{
			for(int b=0;b<j;b++)
			{
				outputs[a][c]+=weightsHF[b][a]*hiddenLayer[b][c];
			}
		}
		for(int a=0;a<i;a++)
		{
			outputs[a][c]=sigmoid(outputs[a][c]);
		}
		return;
	}

	/**
	 * It sets the values of everything in the hidden layer to 0, because
	 * the way I calculate the hidden layer requires that it be cleared every time
	 * since I just keep adding to the hidden layer. So if I don't clear the outputs
	 * they will just keep adding on each other causing a calculation error.
	 * 
	 * This version is different. It clears the hidden layer for just one model.
	 * It helps reduce the amount of calculations during the training process
	 */
	public void clearHidden(int b)
	{
		for(int a=0;a<hiddenLayer.length;a++)
		{
			hiddenLayer[a][b]=0;
		}
		return;
	}

	/**
	 * It sets the values of everything in the output layer to 0, because
	 * the way I calculate the output layer requires that it be cleared every time
	 * since I just keep adding to the output layer. So if I don't clear the outputs
	 * they will just keep adding to each other causing a calculation error.
	 * 
	 * This version is different. It clears the output layer for just one model.
	 * It helps reduce the amount of calculations during the training process
	 */
	public void clearoutputs(int b)
	{
		for(int a=0;a<outputs.length;a++)
		{
			outputs[a][b]=0;
		}
		return;
	}


	/**
	 * This method calculates the error function based on the error function
	 * Error=Sum(Expected output-Actual Output)^2).
	 * 
	 * The error itself is used to show how accurate the network is at predicting
	 * different letters from the training inputs.
	 */
	public void calcError()
	{
		double changeError=0;
		for(int a=0;a<outputs.length;a++)
		{
			for(int b=0;b<outputs[0].length;b++)
			{
				changeError+=Math.pow(trainOutputs[a][b]-outputs[a][b],2);
			}
		}
		error=changeError/2;
		return;
	}

	/**
	 * Prints the relevant information about the network, the weight values, the error, and 
	 * the outputs so that I can see whether or not the network is actually working.
	 */
	public void printInfo()
	{
		System.out.println("Weights Between the Input and Hidden Layers");
		for(int a=0;a<weightsAH.length;a++)
		{
			for(int b=0;b<weightsAH[0].length;b++)
			{
				System.out.print(weightsAH[a][b]);
				System.out.print("  ");
			}
			System.out.println();
		}
		System.out.println("Weights Between the Hidden and outputs Layer");
		for(int a=0;a<weightsHF.length;a++)
		{
			for(int b=0;b<weightsHF[0].length;b++)
			{
				System.out.print(weightsHF[a][b]);
				System.out.print("  ");
			}
			System.out.println();
		}
		System.out.println("Hidden Layer");
		for(int a=0;a<hiddenLayer.length;a++)
		{
			for(int b=0;b<hiddenLayer[0].length;b++)
			{
				System.out.print(hiddenLayer[a][b]);
				System.out.print(" ");
			}
			System.out.println();
		}

		System.out.println("Error:"+error);
		System.out.println("outputs:");
		for(int a=0;a<outputs.length;a++)
		{
			for(int b=0;b<outputs[0].length;b++)
			{
				System.out.print(outputs[a][b]);
				System.out.print(" ");
			}
			System.out.println();
		}
		return;
	}

	/**
	 * Calculates the changes in the weights between the input and hidden layers based
	 * on the given function given in the sheet on Athena. It uses the values of upper case
	 * psi (upperPsi) to calculate these values.
	 * 
	 * It changes the weights for just one layer, which is to try to make the network
	 * as efficient as possible.
	 */
	public void changeAH(int m)
	{
		for(int a=0;a<k;a++)
		{
			for(int b=0;b<j;b++)
			{
				deltaWeightsAH[a][b]=learningConstant*inputs[a][m]*upperPsi[b][m];
			}
		}
		for(int a=0;a<k;a++)
		{
			for(int b=0;b<j;b++)
			{
				weightsAH[a][b]+=deltaWeightsAH[a][b];
			}
		}
		return;
	}

	/**
	 * Clears the psi 2d array. It loops through and sets the values of psi to 0.
	 * This is important because the method that I calculate psi. I will keep
	 * on adding to psi. So, when I train the network, it will not correctly change the
	 * weights, causing a calculation error.
	 * 
	 * Then it:
	 * calculates the values of the psi for each output node. This will be used
	 * in calculating the change in the weights.
	 * The equation for lower case psi is given on the sheet on Athena.
	 */
	public void calcPsi(int b)
	{
		for(int a=0;a<i;a++)
		{
			double thetaI=0;
			for(int a2=0;a2<j;a2++)
			{
				thetaI+=hiddenLayer[a2][b]*weightsHF[a2][a];
			}
			psi[a][b]=(trainOutputs[a][b]-outputs[a][b])*derSigmoid(thetaI);
		}
		return;
	}

	/**
	 * Calculates the value of the upper case Psi. The equation is the one from the sheet on
	 * athena, it uses omega and theta to calculate upper case psi.
	 */
	public void calcUpperPsi(int m)
	{
		for(int b=0;b<j;b++)
		{
			double omega=0.0;
			for(int a1=0;a1<i;a1++)
			{
				omega+=psi[a1][m]*weightsHF[b][a1];
			}
			double theta=0.0;
			for(int b1=0;b1<k;b1++)
			{	
				theta+=inputs[b1][m]*weightsAH[b1][b];
			}
			upperPsi[b][m]=omega*derSigmoid(theta);
		}
		return;
	}

	/**
	 * Calculates the changes in the weights between the hidden and output layers. It
	 * uses the equation given on athena, which uses psi to calculate the changes in the weights.
	 */
	public void changeHF(int c)
	{
		for(int a=0;a<j;a++)
		{
			for(int b=0;b<i;b++)
			{
				deltaWeightsHF[a][b]=learningConstant*hiddenLayer[a][c]*psi[b][c];
			}
		}
		for(int a=0;a<j;a++)
		{
			for(int b=0;b<i;b++)
			{
				weightsHF[a][b]+=deltaWeightsHF[a][b];
			}
		}
		return;
	}

	/**
	 * Calculates many values including psi, upperPsi, then it changes the values of the weights
	 * using the methods previously defined, calcPsi, changeAH, calcUpperPsi,
	 * changeHF, changeWeights.
	 */
	public void calcAndChange()
	{
		for(int a=0;a<12;a++)
		{
			calcNetwork(a);
			calcPsi(a);
			calcUpperPsi(a);
			changeHF(a);
			changeAH(a);
		}
		calcNetwork();
		calcError();
		return;
	}

	/**
	 * This methods sets the weights for the neural network. It is useful because it allows the network
	 * to read in the weights from a file to calculate the final network values.
	 * @param weightsAH the new weights between the input and hidden layer
	 * @param weightsHF the new weights between the hidden and output layer
	 */
	public void setWeights(double[][]weightsAH,double[][]weightsHF)
	{
		this.weightsAH=weightsAH;
		this.weightsHF=weightsHF;
		return;
	}

	/**
	 * This method checks if the input file exists. Then reads them in and returns a 2d array full of the 
	 * inputs
	 * @return a 2d array of all the inputs read in from the file
	 */
	public static double[][] readInputs()
	{
		double[][] testInputs=new double[k][models];
		try {
			Scanner getTrainInputs=new Scanner(new File("trainInputsFacial.txt"));
			for(int a=0;a<testInputs.length;a++)
			{
				for(int b=0;b<testInputs[0].length;b++)
				{
					testInputs[a][b]=getTrainInputs.nextInt();
				}
				getTrainInputs.nextLine();
			}
			getTrainInputs.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("x");
		}
		return testInputs;
	}

	/**
	 * This method reads in the weights from a file if they exist, otherwise it does nothing.
	 */
	public void readInWeights()
	{
		double[][] weightsAHRead=new double[k][j];
		double[][] weightsHFRead=new double[j][i];
		try {
			Scanner scanBox=new Scanner(new File("FacialRecognitionWeights.txt"));
			for(int a=0;a<weightsAHRead.length;a++)
			{
				for(int b=0;b<weightsAHRead[0].length;b++)
				{
					weightsAHRead[a][b]=scanBox.nextDouble();
				}
				scanBox.nextLine();
			}
			scanBox.nextLine();
			for(int a=0;a<weightsHFRead.length;a++)
			{
				for(int b=0;b<weightsHFRead[0].length;b++)
				{
					weightsHFRead[a][b]=scanBox.nextDouble();
				}
				scanBox.nextLine();
			}
			scanBox.close();
			setWeights(weightsAHRead,weightsHFRead);
		}// a try catch statement that tries to read in the weights from a file
		catch (FileNotFoundException e) 
		{
		}
		return;
	}

	/**
	 * This method prints the current weights of the test OCR network out to a file
	 * so that it can be used later.
	 * 
	 * The method helps to save progress or keep the final weights so that the network can
	 * be run without re-training every time
	 * 
	 * @param test
	 */
	public static void printWeights(FacialRecognition test)
	{
		try
		{
			PrintWriter writer=new PrintWriter("FacialRecognitionWeights.txt");
			for(int a=0;a<test.weightsAH.length;a++)
			{
				for(int b=0;b<test.weightsAH[0].length;b++)
				{
					writer.print(test.weightsAH[a][b]+" ");
				}
				writer.println();
			}
			writer.println("hf");
			for(int a=0;a<test.weightsHF.length;a++)
			{
				for(int b=0;b<test.weightsHF[0].length;b++)
				{
					writer.print(test.weightsHF[a][b]+" ");
				}
				writer.println();
			}
			writer.close();
		}//a try statement that tries to print the weights to a file
		catch(FileNotFoundException e)
		{
		}
		return;
	}

	/**
	 * This method takes in the outputs of the network and uses them to figure out which letter
	 * the input is.
	 * 
	 * It does so by going through each input and then checking which output is closest to 1.
	 * After that it goes to the array of letters declared as an instance variable and decides
	 * which letter it is.
	 * 
	 * @param outputs the outputs of the network which this method will analyze to determine which
	 * letter the outputs are.
	 * 
	 * While all the previous methods were applicable to other problems, this method only applies to 
	 * Character recognition, for each problem, a method would have to be created to understand the output
	 * values.
	 */
	public static void figureOutLetters(double[][] outputs)
	{
		for(int a=0;a<models;a++)
		{
			double currValue=0;
			int curr=0;
			for(int b=0;b<i;b++)
			{
				if(outputs[a][b]>currValue)
				{
					curr=b;
					currValue=outputs[a][b];
				}
			}
			System.out.println(names[curr]);
			for(int b=0;b<models;b++)
         {
            if(b!=curr)
            {
               System.out.println(outputs[a][b]);
            }
            else
            {
               System.out.println("Correct: "+outputs[a][b]);
            }
         }
		}//loops over every model and for each model it figures out which letter is given and prints it to the console
		return;
	}

	/**
	 * Reads in the inputs from a file and then creates the array of outputs.
	 * Next it looks for a file with the weights in it. If the file does not exist
	 * then it randomizes the weights until the error is below WEIGHTS_BELOW.
	 * Then it trains the network until the error is below STOP_TRAINING.
	 * After that, whether or not the file with weights exists, it creates
	 * a new network, reads in the weights from a file and calculates all the nodes
	 * in the network with the read in weights. Finally it figures out what the 
	 * outputs from the network mean and which letter they predict the images are.
	 */
	public static void main(String[] args)
	{
		double[][] testInputs;
		double[][] trainoutputs=new double[i][models];
		testInputs=readInputs();
		for(int x=0;x<12;x++)
		{
			trainoutputs[x][x]=1;
		}
		try{
			Scanner scanBox=new Scanner(new File("FacialRecognitionWeights.txt"));
			scanBox.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("done");
			FacialRecognition test=new FacialRecognition(testInputs, trainoutputs);
			test.randWeights();
			test.calcNetwork();
			test.randWeights();
			while(test.error>WEIGHTS_BELOW)
			{
				test.randWeights();
				test.calcNetwork();
				//counter++;
				System.out.println(test.error);
			}
			System.out.println("done rand");
			double lastError=100;
			while(test.error>STOP_TRAINING)
			{
				test.calcAndChange();
				System.out.println (test.error);
				printWeights(test);
				lastError=test.error;
			}
		}//a catch statement that trains the network based on the testInputs and trainoutputs
		FacialRecognition finalOCR=new FacialRecognition(testInputs,trainoutputs);
		finalOCR.readInWeights();
		finalOCR.calcNetwork();
		figureOutLetters(finalOCR.outputs);
		return;
	}
}