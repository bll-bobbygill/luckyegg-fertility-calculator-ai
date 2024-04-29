package com.luckyegg.amhcalculatormojo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.prediction.BinomialModelPrediction;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;


@RestController
@SpringBootApplication
public class FertilityCalculatorMojoApplication {
    private EasyPredictModelWrapper model;

    public FertilityCalculatorMojoApplication() throws Exception {
        // Load the MOJO model from the classpath
        InputStream modelStream = FertilityCalculatorMojoApplication.class.getClassLoader()
        .getResourceAsStream("StackedEnsemble_BestOfFamily_7_AutoML_1_20240418_75305-MOJO");

        
        if (modelStream == null) {
            throw new IllegalStateException("Model file not found in resources.");
        }

        // Write the InputStream to a temporary file which we can then pass into our MOJO loader
        File tempFile = File.createTempFile("model", ".mojo");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = modelStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        // Load the MOJO model from the temporary file
        MojoModel mojo = MojoModel.load(tempFile.getAbsolutePath());
            model = new EasyPredictModelWrapper(mojo);
    }

    // Health check endpoint used by SageMaker
    @GetMapping("/ping")
    public String ping() {
        return "Healthy";
    }

    // Inference endpoint used by SageMaker
    @PostMapping("/invocations")
    public String invocations(@RequestBody PredictionRequest request) throws Exception {
        RowData row = new RowData();
        row.put("age", request.getAge());
        row.put("amh", request.getAmh());

        BinomialModelPrediction prediction = model.predictBinomial(row);
        return Double.toString(prediction.classProbabilities[1]); // Assuming class "1" is the positive class
    }

	public static void main(String[] args) {
		SpringApplication.run(FertilityCalculatorMojoApplication.class, args);
	}

}
class PredictionRequest {
    private String age;
    private String amh;

    // Add getters and setters.
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAmh() {
		return amh;
	}

	public void setAmh(String amh) {
		this.amh = amh;
	}

	public PredictionRequest(String age, String amh) {
		this.age = age;
		this.amh = amh;
	}

    public PredictionRequest() {
    }
}