/**
 * The CharacterRecognition class creates a neural network based on given parameters and is able to 
 * calculate the values for the hidden and outputs layers based on the values of the inputs and 
 * the weights. It follows the guidelines for the class given by the notes given by Dr. Nelson to
 * train the network using Backward Propogation and Steepest Descent. It uses the equations given
 * to loop over certain values to calculate the values of the upper case psi (UpperPsi) and the
 * lower case psi (psi) then uses those values to calculate the changes in the weights for
 * weightsAH and weightsHF. To calculate the upper case psi, I use individual methods to calculate
 * values of theta and omega, as guidelined by the guideline document on athena.
 * 
 * @author Nikhil Singh
 * @version 9-12-14
 *
 */
public class CharacterRecognition {
      //i, the number of nodes in the F layer, always 1 for the XOR gate
      final int i=1;
      
      //number of nodes in the hidden layer
      final int j=40;
      
      //number of nodes in the input layer
      final int k=400;
      
      //arraylist of weights from A to H
      double[][] weightsAH;
      
      //number of models
      final int models=26;
      
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
      final double learningConstant=1;
      
      //stores the error for the network
      double error;
      
      //stores the psi values for each output node
      double [][] psi=new double[i][models];
      
      //stores the values used to calculate the change in weights between the hidden and output 
      //layers
      double [][] upperPsi=new double[j][models];
      
      /**
       * Initializes a neural network with the given inputs and expected outputs.
       * @param inputs is a 2d array with the inputs, it is broken down by each training set
       * @param outputs is a 2d array with the expected outputs, this will be used to train the network
       */
      public CharacterRecognition(double[][] inputs, double[][] outputs)
      {
         this.inputs=inputs;
         this.trainOutputs=outputs;
      }
      
      /**
       * Calculates random weights for the network and adds them to the 
       * array of weights, this is used as a starting point for using steepest descent
       * and back propogation, it initializes the random weights so that from
       * there we can see which direction to go and start to close in on the minimum
       * the weights are initialized for both weightsAH and weightsHF and are given a random
       * value from -5 to 5
       */
      public void randWeights() 
      {
         weightsAH=new double[k][j];
         deltaWeightsAH=new double[k][j];
         for(int a=0;a<k;a++)
         {
            for(int b=0; b<j;b++) 
            {
               weightsAH[a][b]=Math.random()*11-5; 
            }
         }
         weightsHF=new double[j][i];
         deltaWeightsHF=new double[j][i];
         for(int a=0;a<j;a++)
         { 
            for(int b=0; b<i;b++) 
            {
               weightsHF[a][b]=Math.random()*11-5; 
            }
         }
         return;
      }
      
      /** Calculates the sigmoid function using x as the input the sigmoid function is 1/(e^x+1)
       * @param x the input to the sigmoid
       * @return the result of the sigmoid with x in it
       */
      public double sigmoid(double x)
      {
         double toReturn=1/(Math.exp(-x)+1);
         return toReturn;
      }
      
      /** Calculates the derivative of the sigmoid function using x as the input
       * @param x the input to the derivative of the sigmoid
       * @return the result of the derivative of the sigmoid with x in it
       */
      public double derSigmoid(double x)
      {
         double z=sigmoid(x);
         return z*(1-z);
      }
      
      /**
       * Calculates the values in the hidden layer based on inputs and weight
       * For each hidden layer node, it multiplies each input by each weight 
       * relating to the input and the hidden layer node, and
       * applies the activation function or the sigmoid function, then the steps
       * are repeated for each node in the hidden layer
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
       * Calculates the values of the outputs based on the hidden layer
       * For each output layer node, it multiplies each hidden layer node by the weight that
       * relates to the hidden layer node and the output layer node, then
       * for each output layer node, it adds up all of the values and
       * then applies the sigmoid function to them, which is the activation function
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
       * since I just keep adding to the hidden layer, so if I don't clear the outputs
       * they will just keep adding on each other causing a calculation error
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
       * since I just keep adding to the output layer, so if I don't clear the outputs
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
       * the error
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
       * This method calculates the error function based on the error function
       * Error=Sum(Expected output-Actual Output)^2)
       * We use this error function to decide how accurate the weights are in
       * calculating a specific output from the given inputs, compared to the
       * actual inputs that are given when the object is initialized
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
       * the outputs so that I can see whether or not the network is actually working
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
       * calculates the changes in the weights between the input and hidden layers based
       * on the given function given in the sheet on Athena, it uses the values of upper case
       * psi (upperPsi) to calculate these values
       */
      public void changeAH()
      {
         for(int a=0;a<k;a++)
         {
            for(int b=0;b<j;b++)
            {
               for(int m=0;m<models;m++)
               {
                  deltaWeightsAH[a][b]+=learningConstant*inputs[a][m]*upperPsi[b][m];
               }
            }
         }
         return;
      }
      
      /**
       * calculates the values of the psi for each output node, this will be used
       * in calculating the change in the weights
       * The equation for lower case psi is given on the sheet on athena
       */
      public void calcPsi()
      {
         for(int b=0;b<models;b++)
         {
            for(int a=0;a<i;a++)
            {
               double thetaI=0;
               for(int a2=0;a2<j;a2++)
               {
                  thetaI+=hiddenLayer[a2][b]*weightsHF[a2][a];
               }
               psi[a][b]+=(trainOutputs[a][b]-outputs[a][b])*derSigmoid(thetaI);
            }
         }
         return;
      }
      
      /**
       * Calculates the value of the upper case Psi, the equation is the one from the sheet on
       * athena, it uses omega and theta to calculate upper case psi
       */
      public void calcUpperPsi()
      {
         for(int a=0;a<k;a++)
         {
            for(int b=0;b<j;b++)
            {
               for(int m=0;m<models;m++)
               {
                  double theta=calcTheta(b,m);
                  double omega=calcOmega(b,m);
                  upperPsi[b][m]+=omega*derSigmoid(theta);
               }
            }
         }
         return;
      }

      /**
       * calculates the value of theta for a specific hidden node, the equation is given
       * by the sheet on athena
       * @param a is the value of the hidden node that we are calculating theta for
       * @param m is the current model that we are calculating theta for
       */
      public double calcTheta(int a,int m)
      {
         double theta=0;
         for(int b=0;b<k;b++)
         {
            theta+=inputs[b][m]*weightsAH[b][a];
         }
         return theta;
      }
      
      /**
       * calculates the value of omega for a specific hidden node, it uses the equation given
       * by the sheet on Athena.
       * @param a is the current hidden node we are calculating omega for
       * @param m is the current model we are calculating omega for
       */
      public double calcOmega(int a,int m)
      {
         double omega=0;
         for(int a1=0;a1<i;a1++)
         {
            omega+=psi[a1][m]*weightsHF[a][a1];
         }
         return omega;
      }
      
      /**
       * clears the psi 2d array, it loops through and sets the values of psi to 0
       * this is important because the method that I calculate psi, I will keep
       * on adding to psi, so when I train the network, it will not correctly change the
       * weights, causing a calculation error
       */
      public void clearPsi()
      {
         for(int a=0;a<psi.length;a++)
         {
            for(int b=0;b<psi[0].length;b++)
            {
               psi[a][b]=0;
            }
         }
         return;
      }
      
      /**
       * clears the upperPsi 2d array, loops through and sets the value of upperPsi to 0
       * this is important because the way that I calculate the values of the upperPsi
       * 2d array, I continually add to each node, so without clearing it, it would
       * compile every time I use the backward propogation I would have calculation errors
       * and the network would not train correctly
       */
      public void clearUpperPsi()
      {
         for(int a=0;a<upperPsi.length;a++)
         {
            for(int b=0;b<upperPsi[0].length;b++)
            {
               upperPsi[a][b]=0;
            }
         }
         return;
      }
      
      /**
       * calculates the changes in the weights between the hidden and output layers, it
       * uses the equation given on athena, which uses psi to calculate the changes in the weights
       */
      public void changeHF()
      {
         for(int a=0;a<deltaWeightsHF.length;a++)
         {
            for(int b=0;b<deltaWeightsHF[0].length;b++)
            {
               for(int c=0 ; c<models;c++)
               {
                  deltaWeightsHF[a][b]+=learningConstant*hiddenLayer[a][c]*psi[b][c];
               }
            }
         }
         return;
      }
      
      /**
       * changes the weights for weightsAH and weightsHF, based on the stored values
       * in the deltaWeightsAH and the deltaWeightsHF arrays, it just loops through
       * and adds deltaWeightsAH or deltaWeightsHF [a][b] to the weightsAH or 
       * deltaWeightsHF [a][b]
       */
      public void changeWeights()
      {
         for(int a=0;a<weightsAH.length;a++)
         {
            for(int b=0;b<weightsAH[0].length;b++)
            {
               weightsAH[a][b]+=deltaWeightsAH[a][b];
            }
         }
         
         for(int a=0;a<weightsHF.length;a++)
         {
            for(int b=0;b<weightsHF[0].length;b++)
            {
               weightsHF[a][b]+=deltaWeightsHF[a][b];
            }
         }
         return;
      }
      
      /**
       * calculates many values including psi, upperPsi, then it changes the values of the weights
       * using the methods previously defined, clearPsi, calcPsi, changeAH, clearUpperPsi, calcUpperPsi,
       * changeHF, changeWeights
       */
      public void calcAndChange()
      {
         clearPsi();
         calcPsi();
         changeAH();
         clearUpperPsi();
         calcUpperPsi();
         changeHF();
         changeWeights();
         return;
      }
      
      /**
       * runs the class by creating arrays of inputs and outputs, then it makes an object of this
       * class and randomizes the weights, it keeps doing so until the error goes below .25, then after
       * that changes the weights using back prop and steepest descent until the error goes below.001
       * and finally prints out all of the information for the network
       */
      public static void main(String[] args)
      {
         double[][] testInputs=new double[2][4];
         double[][] trainoutputs=new double[1][4];
         
         testInputs[0][0]=0;
         testInputs[1][0]=0;
         testInputs[0][1]=0;
         testInputs[1][1]=1;
         testInputs[0][2]=1;
         testInputs[1][2]=0;
         testInputs[0][3]=1;
         testInputs[1][3]=1;
         
         trainoutputs[0][0]=0;
         trainoutputs[0][1]=1;
         trainoutputs[0][2]=1;
         trainoutputs[0][3]=0;
         
         CharacterRecognition test=new CharacterRecognition(testInputs, trainoutputs);
         test.randWeights();
         test.calcNetwork();
         test.changeAH();
         test.changeHF();
         int counter=0;
         while(test.error>.25)
         {
            test.randWeights();
            test.calcNetwork();
            counter++;
         }
         while(test.error>.001)
         {
            if(counter>10000)
            {
               System.out.println("failure");
               main(args);
               return;
            }
            test.calcAndChange();
            test.calcNetwork();
            counter++;
         }
         
         test.printInfo();
         System.out.println(counter);
         return;
      }
}
      