package com.mani.in.ratelimiter.response;

import com.mani.in.ratelimiter.model.ResponseType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ComputeResponse {

    private int input;
    private long output;
    private ResponseType responseType;
    private String message;
	public static ComputeResponse of(int input2, int i, ResponseType success, String empty) {
		// TODO Auto-generated method stub
		return null;
	}
    
    

}
