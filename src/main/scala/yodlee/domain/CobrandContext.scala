package yodlee.domain

case class Session(cobSession: String)
case class CobrandContext(cobrandId: String, applicationId: String, session: Session)
