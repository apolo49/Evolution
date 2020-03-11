/**
 * 
 */
package entities;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.ActivationLayer;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.GravesLSTM;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.lwjgl.util.vector.Vector3f;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import com.google.common.base.Splitter;

import EngineTest.Game;
import RenderEngine.Loader;
import RenderEngine.OBJLoader;
import fileHandling.log.Logger;
import models.TexturedModel;
import python.PyExecuter;
import textures.ModelTexture;
import toolbox.Random;

/**
 * The initial and base NPC Human.
 * 
 * @author Joe
 *
 */
@SuppressWarnings("deprecation")
public class Human extends Entity{
	
	/**
	 * The Game's loader for the current OpenGL context allowing the model to be made inside the class for the entity.
	 */
	private static Loader loader = Game.getLoader();
	
	/**
	 * The model for the human.
	 */
	
	private static TexturedModel model = new TexturedModel(OBJLoader.loadObjModel("Human1", loader),new ModelTexture(loader.loadTexture("plain")));

	/**
	 * The unique ID for the Human.
	 */
	
	private BigInteger EntityID;
	
	/**
	 * The unique sequence of DNA for the human.
	 */
	
	private int[] DNA;
	
	/**
	 * Current Save in use by the game grabbed from the game class.
	 */
	
	private String CurrentSave = Game.getCurrentSave();
	
	/**
	 * Path to the file generated for this entity.
	 */
	
	private Path pathToEntityFile = Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+CurrentSave+"\\Entities\\"+EntityID+".EvoDNA");
	
	/**
	 * File with the path to the latest log.
	 */
	
	private File logFile = new File(System.getenv("APPDATA")+"\\Evolution\\logs\\Latest.txt");
	
	
	
	private double Intelligence;
	
	private double Attractivity;
	
	private double SocialAbility;
	
	private double Influence;
	
	private MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().weightInit(WeightInit.XAVIER).activation(Activation.RELU)
			.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).updater(Updater.SGD)
			.layer(new DenseLayer.Builder().nIn(720).nOut(250).weightInit(WeightInit.XAVIER).build())
			.layer(new ActivationLayer())
			.layer(new ConvolutionLayer.Builder(1,1).nIn(1024).nOut(2048).stride(1,1)
		    .convolutionMode(ConvolutionMode.Same).weightInit(WeightInit.XAVIER).activation(Activation.IDENTITY).build())
			.layer(new GravesLSTM.Builder().activation(Activation.TANH).nIn(720).nOut(100).build())
		    .layer(new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD).weightInit(WeightInit.XAVIER).activation(Activation.SOFTMAX).nIn(250).nOut(500)
		    .build()).list().backpropType(BackpropType.Standard).build();
	
	/**
	 * Creates the human applying the initial attributes for the human.
	 * 
	 * @param position
	 * 		The position of the human when first spawned.
	 * @param rotX
	 * 		The rotation of the human when first spawned along the X axis.
	 * @param rotY
	 * 		The rotation of the human when first spawned along the Y axis.
	 * @param rotZ
	 * 		The rotation of the human when first spawned along the Z axis.
	 * @param scale
	 * 		The scale of the human when first spawned.
	 */
	
	public Human(Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		
		super(model, position, rotX, rotY, rotZ, scale);
		
		Random random = new Random();
		BigInteger EntityID = random.nextBigInteger();
		
		this.EntityID = EntityID;
		
		PyExecuter.main(null, "test.py "+EntityID.toString()+" "+CurrentSave);
		
		pathToEntityFile = Paths.get(System.getenv("APPDATA")+"\\Evolution\\saves\\"+CurrentSave+"\\Entities\\"+EntityID+".EvoDNA");
		List<String> DNAStringList = null;
		try {
			DNAStringList = Files.readAllLines(pathToEntityFile);
		} catch (IOException e) {
			Logger.IOSevereErrorHandler(e, logFile);
		}
		DNAStringList = Splitter.fixedLength(4).splitToList(DNAStringList.get(0));
		String[] DNAStringArray = DNAStringList.stream().toArray(String[]::new);
		this.DNA = new int[DNAStringArray.length];
		this.DNA = Arrays.asList(DNAStringArray).stream().mapToInt(Integer::parseInt).toArray();
		
		this.Intelligence = Math.abs((DNA[1250]+DNA[560]+DNA[1]))/Math.round(Math.abs((DNA[1250]+DNA[0]+DNA[1])/3));
		this.Attractivity = Math.abs(DNA[1250]*DNA[120]/DNA[250]/1000)*(Intelligence/10);
		this.SocialAbility = Math.abs(Intelligence*Attractivity/100)*((DNA[500]/10000)+0.5f);
		this.Influence = Math.abs(Intelligence*Attractivity*SocialAbility);
		
		MultiLayerNetwork neuralNetwork = new MultiLayerNetwork(conf);
		neuralNetwork.fit();
		
	}

	/**
	 * @return the entityID.
	 */
	public BigInteger getEntityID() {
		return EntityID;
	}

	/**
	 * @param entityID the entityID to set.
	 */
	public void setEntityID(BigInteger entityID) {
		EntityID = entityID;
	}

	/**
	 * @return the DNA.
	 */
	public int[] getDNA() {
		return DNA;
	}

	/**
	 * @param DNA the DNA to set.
	 */
	public void setDNA(int[] DNA) {
		this.DNA = DNA;
	} 
	
	/**
	 * @param Intelligence the Intelligence to set.
	 */
	
	public void setIntelligence(double Intelligence) {
		this.Intelligence = Math.abs(Intelligence);
	}
	
	/**
	 * @return the Intelligence.
	 */
	
	public double getIntelligence() {
		return Intelligence;
	}
	
	/**
	 * Updates the intelligence of the human.
	 * @param modifier The modifier to multiply the intelligence by.
	 */
	
	public void UpdateIntelligence(double modifier) {
		this.Intelligence = Math.abs(Intelligence*modifier);
		updateAttractivity();
	}

	/**
	 * @return the attractivity
	 */
	public double getAttractivity() {
		return Attractivity;
	}

	/**
	 * @param attractivity the attractivity to set
	 */
	public void setAttractivity(double attractivity) {
		Attractivity = attractivity;
	}
	
	private void updateAttractivity() {
		this.Attractivity = Math.abs(DNA[1250]*DNA[120]/DNA[1]/100*Intelligence);
	}

	/**
	 * @return the socialAbility
	 */
	public double getSocialAbility() {
		return SocialAbility;
	}

	/**
	 * @param socialAbility the socialAbility to set
	 */
	public void setSocialAbility(double socialAbility) {
		SocialAbility = Math.abs(socialAbility);
	}

	/**
	 * @return the influence
	 */
	public double getInfluence() {
		return Influence;
	}

	/**
	 * @param influence the influence to set
	 */
	public void setInfluence(double influence) {
		Influence = Math.abs(influence);
	}
	
	public String getStats() {
		return "Intelligence: "+Intelligence+
				"\nAttractiveness "+Attractivity+
				"\nSocial Ability: "+SocialAbility+
				"\nInfluence: "+Influence;
	}
	
}
