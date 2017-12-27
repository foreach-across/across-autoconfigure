package ax.modules.websocket.messages;

/**
 * @author Steven Gentens
 */
public class FarewellMessage
{
	private String content;

	public FarewellMessage() {
	}

	public FarewellMessage( String content ) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}
