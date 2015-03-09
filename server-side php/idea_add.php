<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
echo "Connected Successfully";
$title=$_GET["title"];
$username=$_GET["username"];
$descrip=$_GET["descrip"];
//$userid=$_GET["userid"];
$cat=$_GET["cat"];
$sub=$_GET["sub"];
$lat=$_GET["lat"];
$long=$_GET["long"];
$state=$_GET["state"];
$country=$_GET["country"];
$title= stripslashes($title);
$username= stripslashes($username);
$descrip= stripslashes($descrip);
$cat= stripslashes($cat);
$sub= stripslashes($sub);
$lat=stripslashes($lat);
$long=stripslashes($long);
$state=stripslashes($state);
$country=stripslashes($country);
echo "<br> your title is " . $title . ",username is " . $username . ",descrip is " . $descrip . ",cat is " . $cat . ",subcat is ". $sub;
echo "<br> your lat is " . $lat . ",long is " . $long . ",state is " . $state . ",country is " . $country;
$query=$conn->query("SELECT user FROM user WHERE author='$username'");
$fetch=$query->fetch();
$userid=$fetch['user'];
echo "<br> user id is: " . $userid;
$stmt=$conn->prepare("INSERT INTO ideas (title,author,descrip,iduser,time,cat,sub) VALUES ('$title','$username','$descrip','$userid',NOW(),'$cat','$sub')");
if($stmt->execute())
{
	echo "<br>idea has been INSERT INTO successfully";
}else
{
echo "<br>idea insertion fails";
}
$ideaid=$conn->lastInsertID();
echo "<br> idea insert id: " . $ideaid;
$stmt=$conn->prepare("INSERT INTO location (ididea,lat,lng,state,country,iduser) VALUES ('$ideaid','$lat','$long','$state','$country','$userid')");
if($stmt->execute())
{
	echo "<br>location has been INSERT INTO successfully";
}else
{
echo "<br>idea insertion fails";
}
} catch(PDOException $e) {
    echo 'ERROR: ' . $e->getMessage();
}
?>

