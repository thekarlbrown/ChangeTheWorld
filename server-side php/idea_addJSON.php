<?php
try{
$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$title=$_GET["title"];
$username=$_GET["username"];
$descrip=$_GET["descrip"];
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
$query=$conn->query("SELECT user FROM user WHERE author='$username'");
$fetch=$query->fetch();
$userid=$fetch['user'];

$stmt=$conn->prepare("INSERT INTO ideas (title,author,descrip,iduser,time,cat,sub) VALUES ('$title','$username','$descrip','$userid',NOW(),'$cat','$sub')");
if($stmt->execute())
{
	echo "[{\"added\":\"1\"}]";
}else
{
	echo "[{\"added\":\"0\"}]";
}
$ideaid=$conn->lastInsertID();
$stmt=$conn->prepare("INSERT INTO location (ididea,lat,lng,state,country,iduser) VALUES ('$ideaid','$lat','$long','$state','$country','$userid')");
$stmt->execute();

} catch(PDOException $e) {
    echo 'ERROR: ' . $e->getMessage();
}
?>

