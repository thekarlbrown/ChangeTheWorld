<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	echo "Connected Successfully<br>";
	$ideaid=$_GET["ideaid"];
	$username=$_GET["username"];
	$ideaid = stripslashes($ideaid);
	$username = stripslashes($username);
	
	$query=$conn->query("SELECT user FROM user WHERE author='$username'");
	$fetch=$query->fetch();
	$userid=$fetch['user'];
	
	$query=$conn->query("SELECT iduser FROM ideas WHERE ididea='$ideaid'");
	$fetch=$query->fetch();
	$friendid=$fetch['iduser'];
	
	$data = $conn->query("SELECT fav,rated FROM favratd WHERE ididea='$ideaid' AND iduser='$userid'");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	$result=($data->rowCount()>0);
	if($result){
		foreach ($data as $row){
			echo json_encode($row);
		}
	}else{
		echo "{\"fav\":\"null\",\"rated\":\"null\"}";
	}	
	$data = $conn->query("SELECT idfriend FROM friends WHERE idfriend='$friendid' AND iduser='$userid'");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	$result2=($data->rowCount()>0);
	if($result){
		echo "{\"followed\":\"1\"}";
	}else{
		echo "{\"followed\":\"0\"}";
	}
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>