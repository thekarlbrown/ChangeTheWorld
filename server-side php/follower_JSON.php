<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	
	$username=$_GET["username"];
	$follower=$_GET["follower"];
	$case=$_GET["case"];
	
	$query=$conn->prepare("SELECT user FROM user WHERE author=?");
	$query->execute(array($username));
	$fetch=$query->fetch(PDO::FETCH_ASSOC);
	$userid=$fetch['user'];
	
	$query=$conn->prepare("SELECT user FROM user WHERE author=?");
	$query->execute(array($follower));
	$fetch=$query->fetch(PDO::FETCH_ASSOC);
	$followerid=$fetch['user'];

	if($case==1){
		$stmt=$conn->prepare("INSERT IGNORE friends (iduser, idfriend) VALUES (:userid,:followerid)");
	}else if ($case==0){
		$stmt=$conn->prepare("DELETE FROM friends WHERE iduser=:userid AND idfriend=:followerid");
	}
	$stmt->bindValue(':userid',$userid,PDO::PARAM_STR);
	$stmt->bindValue(':followerid',$followerid,PDO::PARAM_STR);
	if($stmt->execute())
	{
		echo "[{\"succeeded\":\"1\"}]"; 
	}else
	{
		echo "[{\"succeeded\":\"0\"}]"; 
	}

} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>