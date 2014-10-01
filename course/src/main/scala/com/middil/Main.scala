package com.middil

import com.middil.api.Api
import com.middil.core.{BootedCore, CoreActors, Core}
import com.middil.web.Web

object Main extends App with Web with Api with CoreActors with Core with BootedCore