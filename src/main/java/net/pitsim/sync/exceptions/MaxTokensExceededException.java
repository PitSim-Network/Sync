package net.pitsim.sync.exceptions;

public class MaxTokensExceededException extends PitException {

	public boolean isRare;

	public MaxTokensExceededException(boolean isRare) {

		this.isRare = isRare;
	}
}
