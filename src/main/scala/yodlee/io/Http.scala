/**
  * ING - CONFIDENTIAL
  * ---------------------------------
  * Copyright (C) 2017 ING Groep N.V.
  * All rights reserved.
  * ---------------------------------
  */
package yodlee.io

import java.io.{BufferedReader, DataOutputStream, IOException, InputStreamReader}
import java.net.{HttpURLConnection, URISyntaxException, URL}
import java.util

object Http {
  private val fqcn = this.getClass.getName
  private val userAgent = "Mozilla/5.0"
  private val contentTypeURLENCODED = "application/x-www-form-urlencoded"
  private val contentTypeJSON = "application/json"

  @throws[IOException]
  def doPost(url: String, requestBody: String): String = {
    val mn = "doIO(POST : " + url + ", " + requestBody + " )"
    System.out.println(fqcn + " :: " + mn)
    val restURL = new URL(url)
    val conn = restURL.openConnection.asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("POST")
    conn.setRequestProperty("User-Agent", userAgent)
    //conn.setRequestProperty("Content-Type", contentTypeURLENCODED);
    conn.setRequestProperty("Content-Type", contentTypeJSON)
    conn.setDoOutput(true)
    val wr = new DataOutputStream(conn.getOutputStream)
    wr.writeBytes(requestBody)
    wr.flush()
    wr.close()
    val responseCode = conn.getResponseCode
    System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP POST' request")
    System.out.println(fqcn + " :: " + mn + " : " + "Response Code : " + responseCode)
    val in = new BufferedReader(new InputStreamReader(conn.getInputStream))
    val jsonResponse = new StringBuilder

    var inputLine = in.readLine
    do {
      println(inputLine)
      jsonResponse.append(inputLine)
      inputLine = in.readLine()
    } while (inputLine != null)

    in.close()
    System.out.println(fqcn + " :: " + mn + " : " + jsonResponse.toString)
    jsonResponse.toString()
  }

  @throws[IOException]
  def doPostUser(url: String, sessionTokens: util.Map[String, String], requestBody: String, isEncodingNeeded: Boolean): String = {
    val mn = "doIO(POST : " + url + ", " + requestBody + "sessionTokens : " + sessionTokens + " )"
    System.out.println(fqcn + " :: " + mn)
    val restURL = new URL(url)
    val conn = restURL.openConnection.asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("POST")
    conn.setRequestProperty("User-Agent", userAgent)
    if (isEncodingNeeded) conn.setRequestProperty("Content-Type", contentTypeJSON)
    else conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8")
    conn.setRequestProperty("Authorization", sessionTokens.toString)
    conn.setDoOutput(true)
    val wr = new DataOutputStream(conn.getOutputStream)
    wr.writeBytes(requestBody)
    wr.flush()
    wr.close()
    val responseCode = conn.getResponseCode
    System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP POST' request")
    System.out.println(fqcn + " :: " + mn + " : " + "Response Code : " + responseCode)
    val in = new BufferedReader(new InputStreamReader(conn.getInputStream))
    val jsonResponse = new StringBuilder

    var inputLine = in.readLine
    do {
      println(inputLine)
      jsonResponse.append(inputLine)
      inputLine = in.readLine()
    } while (inputLine != null)

    in.close()
    System.out.println(fqcn + " :: " + mn + " : " + jsonResponse.toString)
    jsonResponse.toString()
  }

  @throws[IOException]
  @throws[URISyntaxException]
  def doGet(url: String, sessionTokens: util.Map[String, String]): String = {
    val mn = "doIO(GET :" + url + ", sessionTokens =  " + sessionTokens.toString + " )"
    System.out.println(fqcn + " :: " + mn)
    val myURL = new URL(url)
    System.out.println(fqcn + " :: " + mn + ": Request URL=" + url.toString)
    val conn = myURL.openConnection.asInstanceOf[HttpURLConnection]
    //conn.setRequestMethod("GET");
    conn.setRequestProperty("User-Agent", userAgent)
    //conn.setRequestProperty("Content-Type", contentTypeJSON);
    //conn.setRequestProperty("Accept",);
    conn.setRequestProperty("Authorization", sessionTokens.toString)
    conn.setDoOutput(true)
    System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP GET' request")
    val responseCode = conn.getResponseCode
    System.out.println(fqcn + " :: " + mn + " : " + "Response Code : " + responseCode)
    val in = new BufferedReader(new InputStreamReader(conn.getInputStream))
    val jsonResponse = new StringBuilder()

    var inputLine = in.readLine
    do {
      println(inputLine)
      jsonResponse.append(inputLine)
      inputLine = in.readLine()
    } while (inputLine != null)

    in.close()
    System.out.println(fqcn + " :: " + mn + " : " + jsonResponse.toString)
    jsonResponse.toString()
  }

  @throws[IOException]
  @throws[URISyntaxException]
  def doPut(url: String, param: String, sessionTokens: util.Map[String, String]): String = {
    val mn = "doIO(PUT :" + url + ", sessionTokens =  " + sessionTokens.toString + " )"
    System.out.println(fqcn + " :: " + mn)
    val param2 = param.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(",", "%2C").replace("[", "%5B").replace("]", "%5D").replace(":", "%3A").replace(" ", "+")
    val processedURL = url + "?MFAChallenge=" + param2
    //"%7B%22loginForm%22%3A%7B%22formType%22%3A%22token%22%2C%22mfaTimeout%22%3A%2299380%22%2C%22row%22%3A%5B%7B%22id%22%3A%22token_row%22%2C%22label%22%3A%22Security+Key%22%2C%22form%22%3A%220001%22%2C%22fieldRowChoice%22%3A%220001%22%2C%22field%22%3A%5B%7B%22id%22%3A%22token%22%2C%22name%22%3A%22tokenValue%22%2C%22type%22%3A%22text%22%2C%22value%22%3A%22123456%22%2C%22isOptional%22%3Afalse%2C%22valueEditable%22%3Atrue%2C%22maxLength%22%3A%2210%22%7D%5D%7D%5D%7D%7D";
    val myURL = new URL(processedURL)
    System.out.println(fqcn + " :: " + mn + ": Request URL=" + processedURL.toString)
    val conn = myURL.openConnection.asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("PUT")
    conn.setRequestProperty("Accept-Charset", "UTF-8")
    conn.setRequestProperty("Content-Type", contentTypeURLENCODED)
    conn.setRequestProperty("Authorization", sessionTokens.toString)
    conn.setDoOutput(true)
    System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP PUT' request")
    val responseCode = conn.getResponseCode
    System.out.println(fqcn + " :: " + mn + " : " + "Response Code : " + responseCode)
    val in = new BufferedReader(new InputStreamReader(conn.getInputStream))
    val jsonResponse = new StringBuilder

    var inputLine = in.readLine
    do {
      println(inputLine)
      jsonResponse.append(inputLine)
      inputLine = in.readLine()
    } while (inputLine != null)

    in.close()
    println(fqcn + " :: " + mn + " : " + jsonResponse.toString)
    jsonResponse.toString()
  }

  @throws[IOException]
  @throws[URISyntaxException]
  def doPutNew(url: String, param: String, sessionTokens: util.Map[String, String]): String = {
    val mn = "doIO(PUT :" + url + ", sessionTokens =  " + sessionTokens.toString + " )"
    System.out.println(fqcn + " :: " + mn)
    //param=param.replace("\"", "%22").replace("{", "%7B").replace("}", "%7D").replace(",", "%2C").replace("[", "%5B").replace("]", "%5D").replace(":", "%3A").replace(" ", "+");
    val processedURL = url
    //+"?MFAChallenge="+param;//"%7B%22loginForm%22%3A%7B%22formType%22%3A%22token%22%2C%22mfaTimeout%22%3A%2299380%22%2C%22row%22%3A%5B%7B%22id%22%3A%22token_row%22%2C%22label%22%3A%22Security+Key%22%2C%22form%22%3A%220001%22%2C%22fieldRowChoice%22%3A%220001%22%2C%22field%22%3A%5B%7B%22id%22%3A%22token%22%2C%22name%22%3A%22tokenValue%22%2C%22type%22%3A%22text%22%2C%22value%22%3A%22123456%22%2C%22isOptional%22%3Afalse%2C%22valueEditable%22%3Atrue%2C%22maxLength%22%3A%2210%22%7D%5D%7D%5D%7D%7D";
    val myURL = new URL(processedURL)
    System.out.println(fqcn + " :: " + mn + ": Request URL=" + processedURL.toString)
    val conn = myURL.openConnection.asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("PUT")
    conn.setRequestProperty("Accept-Charset", "UTF-8")
    conn.setRequestProperty("Content-Type", contentTypeJSON)
    conn.setRequestProperty("Authorization", sessionTokens.toString)
    conn.setDoOutput(true)
    val wr = new DataOutputStream(conn.getOutputStream)
    wr.writeBytes(param)
    wr.flush()
    wr.close()
    System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP PUT' request")
    val responseCode = conn.getResponseCode
    System.out.println(fqcn + " :: " + mn + " : " + "Response Code : " + responseCode)
    val in = new BufferedReader(new InputStreamReader(conn.getInputStream))
    val jsonResponse = new StringBuilder

    var inputLine = in.readLine
    do {
      println(inputLine)
      jsonResponse.append(inputLine)
      inputLine = in.readLine()
    } while (inputLine != null)

    in.close()
    System.out.println(fqcn + " :: " + mn + " : " + jsonResponse.toString)
    jsonResponse.toString()
  }

  @throws[IOException]
  def doPostRegisterUser(url: String, registerParam: String, cobTokens: util.Map[String, String], isEncodingNeeded: Boolean): String = { //String processedURL = url+"?registerParam="+registerParam;
    /* String registerUserJson="{"
            + "		\"user\" : {"
            + "				\"loginName\" : \"TestT749\","
            + "				\"password\" : \"TESTT@123\","
            + "				\"email\" : \"testet@yodlee.com\","
            + "				\"firstName\" : \"Rehhsty\","
            + "				\"lastName\" :\"ysl\""
            +"             } ,"

            + "		\"preference\" : {"
            + "		\"address\" : {"
            + "				\"street\" : \"abcd\","
            + "				\"state\" : \"CA\","
            + "				\"city\" : \"RWS\","
            + "				\"postalCode\" : \"98405\","
            + "				\"countryIsoCode\" : \"USA\""
            +"             } ,"
            + "				\"currency\" : \"USD\","
            + "				\"timeZone\" : \"PST\","
            + "				\"dateFormat\" : \"MM/dd/yyyy\""
            + "}"
            + "}";*/
    //encoding url
    val registerParam2 = java.net.URLEncoder.encode(registerParam, "UTF-8")
    val processedURL = url + "?registerParam=" + registerParam2
    val mn = "doIO(POST : " + processedURL + ", " + registerParam2 + "sessionTokens : " + " )"
    System.out.println(fqcn + " :: " + mn)
    val restURL = new URL(processedURL)
    val conn = restURL.openConnection.asInstanceOf[HttpURLConnection]
    conn.setRequestMethod("POST")
    conn.setRequestProperty("User-Agent", userAgent)
    conn.setRequestProperty("Content-Type", contentTypeURLENCODED)
    conn.setRequestProperty("Content-Type", "text/plain;charset=UTF-8")
    conn.setRequestProperty("Authorization", cobTokens.toString)
    conn.setDoOutput(true)
    conn.setRequestProperty("Accept-Charset", "UTF-8")
    val responseCode = conn.getResponseCode
    if (responseCode == 200) {
      System.out.println(fqcn + " :: " + mn + " : " + "Sending 'HTTP POST' request")
      System.out.println(fqcn + " :: " + mn + " : " + "Response Code : " + responseCode)
      val in = new BufferedReader(new InputStreamReader(conn.getInputStream))
      val jsonResponse = new StringBuilder

      var inputLine = in.readLine
      do {
        println(inputLine)
        jsonResponse.append(inputLine)
        inputLine = in.readLine()
      } while (inputLine != null)

      in.close()
      System.out.println(fqcn + " :: " + mn + " : " + jsonResponse.toString)
      jsonResponse.toString()
    }
    else {
      System.out.println("Invalid input")
      new String
    }
  }
}
