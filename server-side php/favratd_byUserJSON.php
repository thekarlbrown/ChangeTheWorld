<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$ideaid=$_GET["ideaid"];
	$username=$_GET["username"];
	
	$query=$conn->prepare("SELECT user FROM user WHERE author=?");
	$query->execute(array($username));
	$fetch=$query->fetch(PDO::FETCH_ASSOC);
	$userid=$fetch['user'];
	
	
	$query=$conn->prepare("SELECT iduser FROM ideas WHERE ididea=?");
	$query->execute(array($ideaid));
	$fetch=$query->fetch(PDO::FETCH_ASSOC);
	$friendid=$fetch['iduser'];
	echo "[";
	$data = $conn->prepare("SELECT fav,rated FROM favratd WHERE ididea=:ideaid AND iduser=:userid");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	$data->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
	$data->bindValue(':userid',$userid,PDO::PARAM_INT);
	$data->execute();
	$data_array=$data->fetchAll();
	if($data_array){
			echo json_encode($data_array[0]);
	}else{
		echo "{\"fav\":\"null\",\"rated\":\"null\"}";
	}
	echo ",";	
	$data = $conn->prepare ("SELECT idfriend FROM friends WHERE idfriend=:friendid AND iduser=:userid");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	$data->bindValue(':friendid',$friendid,PDO::PARAM_INT);
	$data->bindValue(':userid',$userid,PDO::PARAM_INT);
	$data->execute();
	$data_array=$data->fetchAll();

	if($data_array){
		echo "{\"followed\":\"1\"}";
	}else{
		echo "{\"followed\":\"0\"}";
	}
	echo "]";
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>