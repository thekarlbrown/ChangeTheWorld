<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	echo "Connected Successfully";
	$username=$_GET["username"];
	$friend=$_GET["friend"];
	$username = stripslashes($username);
	$friend=stripslashes($friend);
	
	$query=$conn->query("SELECT user FROM user WHERE author='$username'");
	$fetch=$query->fetch();
	$userid=$fetch['user'];
	echo "<br> user id is: " . $userid;
	
	$query=$conn->query("SELECT user FROM user WHERE author='$friend'");
	$fetch=$query->fetch();
	$friendid=$fetch['user'];
	echo "<br> friend id is: " . $friendid;

	$stmt=$conn->prepare("DELETE FROM friends WHERE iduser='$userid' AND idfriend='$friendid'");
	if($stmt->execute())
	{
		echo "<br>" . $username . " attempted to remove " . $friend; 
	}else
	{
		echo "<br>friend deletion fails";
	}

} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>