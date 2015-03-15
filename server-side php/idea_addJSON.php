<?php
try{
$conn = new PDO('mysql:host=localhost;dbname=mydb;charset=utf8','root','CENSORED');
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


$query=$conn->prepare("SELECT user FROM user WHERE author=?");
$query->execute(array($username));
$fetch=$query->fetch(PDO::FETCH_ASSOC);
$userid=$fetch['user'];

$stmt=$conn->prepare("INSERT INTO ideas (title,author,descrip,iduser,time,cat,sub) VALUES (:title,:username,:descrip,:userid,NOW(),:cat,:sub)");
$stmt->bindValue(':title',$title,PDO::PARAM_STR);
$stmt->bindValue(':username',$username,PDO::PARAM_STR);
$stmt->bindValue(':descrip',$descrip,PDO::PARAM_STR);
$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
$stmt->bindValue(':cat',$cat,PDO::PARAM_INT);
$stmt->bindValue(':sub',$sub,PDO::PARAM_INT);
if($stmt->execute())
{
	echo "[{\"added\":\"1\"}]";
}else
{
	echo "[{\"added\":\"0\"}]";
}
$ideaid=$conn->lastInsertID();
$stmt=$conn->prepare("INSERT INTO location (ididea,lat,lng,state,country,iduser) VALUES (:ideaid,:lat,:long,:state,:country,:userid)");
$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
$stmt->bindValue(':lat',$lat,PDO::PARAM_STR);
$stmt->bindValue(':long',$long,PDO::PARAM_STR);
$stmt->bindValue(':state',$state,PDO::PARAM_STR);
$stmt->bindValue(':country',$country,PDO::PARAM_STR);
$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
$stmt->execute();

} catch(PDOException $e) {
    echo 'ERROR: ' . $e->getMessage();
}
?>

