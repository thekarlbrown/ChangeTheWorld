<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$lat=$_GET["lat"];
	$long=$_GET["long"];
	$state=$_GET["state"];
	$country=$_GET["country"];
	$username=$_GET["username"];
	$case=$_GET["case"];
	$lat=stripslashes($lat);
	$long=stripslashes($long);
	$state=stripslashes($state);
	$country=stripslashes($country);
	$case = stripslashes($case);
	$username = stripslashes($username);
	$query=$conn->query("SELECT user FROM user WHERE author='$username'");
	$fetch=$query->fetch();
	$userid=$fetch['user'];	
	if($case==0){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas LEFT JOIN location on ideas.ididea=location.ididea WHERE country='$country' 
		AND (SQRT(POW(RADIANS('$lat') - RADIANS(location.lat),2) + POW(((RADIANS('$long') - RADIANS(location.lng)) * (COS((RADIANS('$lat') + RADIANS(location.lat))/2))),2))*3959) < 25  ORDER BY time DESC;");
	}else if($case==1){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas LEFT JOIN location on ideas.ididea=location.ididea WHERE country='$country' 
		AND (SQRT(POW(RADIANS('$lat') - RADIANS(location.lat),2) + POW(((RADIANS('$long') - RADIANS(location.lng)) * (COS((RADIANS('$lat') + RADIANS(location.lat))/2))),2))*3959) < 100  ORDER BY time DESC;");
	}else if($case==2){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas LEFT JOIN location on ideas.ididea=location.ididea WHERE country='$country' AND state='$state' ORDER BY time DESC");
	}else if($case==3){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas LEFT JOIN location on ideas.ididea=location.ididea WHERE country='$country' ORDER BY time DESC");
	}else if($case==4){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas ORDER BY time DESC");
	}else if ($case==5){
		$data=$conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas LEFT JOIN friends ON ideas.iduser=friends.idfriend WHERE friends.iduser='$userid' ");
	}
	
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