<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb;charset=utf8','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$username=$_GET["username"];
	
	$query=$conn->prepare("SELECT user FROM user WHERE author=?");
	$query->execute(array($username));
	$fetch=$query->fetch(PDO::FETCH_ASSOC);
	$userid=$fetch['user'];
	
	$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas LEFT JOIN favratd ON ideas.ididea=favratd.ididea WHERE favratd.iduser='$userid' AND favratd.fav='1'");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	$data_array=$data->fetchAll();
	echo "[";
	$count=count($data_array)-1;
	for($i=0;$i<$count+1;$i++){
		echo json_encode($data_array[$i]);
		if($i<$count){
			echo",";
		}
	}
	echo "]";
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>
