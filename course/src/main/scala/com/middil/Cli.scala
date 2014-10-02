import com.middil.api.Api
import com.middil.core.{CoreActors, BootedCore}
import com.middil.web.Web

object Cli extends App with BootedCore with CoreActors with Api with Web
