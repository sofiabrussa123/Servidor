package globales;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputManager implements InputProcessor {

    private boolean isWPressed = false;
    private boolean isAPressed = false;
    private boolean isSPressed = false;
    private boolean isDPressed = false;
    private boolean isUpPressed = false;
    private boolean isLeftPressed = false;
    private boolean isDownPressed = false;
    private boolean isRightPressed = false;
    private boolean isEscPressed = false;
    private boolean isPPressed = false;
    private boolean isEPressed = false;
    private boolean isOPressed = false;

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
        	case Input.Keys.W:  isWPressed = true; break;
        	case Input.Keys.A:  isAPressed = true; break;
        	case Input.Keys.S:  isSPressed = true; break;
        	case Input.Keys.D:  isDPressed = true; break;
        	case Input.Keys.UP:  isUpPressed = true; break;
        	case Input.Keys.LEFT:  isLeftPressed = true; break;
        	case Input.Keys.DOWN:  isDownPressed = true; break;
        	case Input.Keys.RIGHT:  isRightPressed = true; break;
        	case Input.Keys.ESCAPE:  isEscPressed = true; break;
        	case Input.Keys.P:  isPPressed = true; break;
        	case Input.Keys.E: isEPressed = true; break;
        	case Input.Keys.O: isOPressed = true; break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
    	switch(keycode) {
	    	case Input.Keys.W:  isWPressed = false; break;
	    	case Input.Keys.A:  isAPressed = false; break;
	    	case Input.Keys.S:  isSPressed = false; break;
	    	case Input.Keys.D:  isDPressed = false; break;
	    	case Input.Keys.UP:  isUpPressed = false; break;
        	case Input.Keys.LEFT:  isLeftPressed = false; break;
        	case Input.Keys.DOWN:  isDownPressed = false; break;
        	case Input.Keys.RIGHT:  isRightPressed = false; break;
	    	case Input.Keys.ESCAPE:  isEscPressed = false; break;
	    	case Input.Keys.P:  isPPressed = false; break;
	    	case Input.Keys.E: isEPressed = false; break;
	    	case Input.Keys.O: isOPressed = false; break;
    	}

        return false;
    }

    public void resetPauseKeys() {
    	isEscPressed = false;
    	isPPressed = false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public boolean getIsWPressed() {
        return isWPressed;
    }

    public boolean getIsAPressed() {
        return isAPressed;
    }

    public boolean getIsSPressed() {
        return isSPressed;
    }

    public boolean getIsDPressed() {
        return isDPressed;
    }

    public boolean getIsUpPressed() {
        return isUpPressed;
    }

    public boolean getIsLeftPressed() {
        return isLeftPressed;
    }

    public boolean getIsDownPressed() {
        return isDownPressed;
    }

    public boolean getIsRightPressed() {
        return isRightPressed;
    }

    public boolean getIsEscPressed() {
        return isEscPressed;
    }

    public boolean getIsPPressed() {
        return isPPressed;
    }

    public boolean getIsEPressed() {
        return isEPressed;
    }

    public boolean getIsOPressed() {
        return isOPressed;
    }

}
