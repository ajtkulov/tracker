import javax.inject.Inject

import play.api.http.DefaultHttpFilters
import utils.AuditFilter

class Filters @Inject()(log: AuditFilter) extends DefaultHttpFilters(log)
