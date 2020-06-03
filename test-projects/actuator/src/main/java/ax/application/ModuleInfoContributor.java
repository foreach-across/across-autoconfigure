package ax.application;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

/**
 * @author Arne Vandamme
 * @since 2.0.0
 */
@Component
public class ModuleInfoContributor implements InfoContributor
{
	@Override
	public void contribute( Info.Builder builder ) {
		builder.withDetail( "module.says", "hello" );
	}
}
