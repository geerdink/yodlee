package yodlee.domain

case class Session(cobSession: String)
case class CobrandContext(cobrandId: String, applicationId: String, session: Session)

case class UserSession(userSession: String)
case class User(id: String, loginName: String, session: UserSession)
case class UserContext(user: User, session: UserSession)