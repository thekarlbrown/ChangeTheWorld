<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb;charset=utf8','root','sqlDEB123');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$lat=$_GET["lat"];
	$long=$_GET["long"];
	$state=$_GET["state"];
	$country=$_GET["country"];
	$username=$_GET["username"];
	$case=$_GET["case"];
	
	$query=$conn->prepare("SELECT user FROM user WHERE author=?");
	$query->execute(array($username));
	$fetch=$query->fetch(PDO::FETCH_ASSOC);
	$userid=$fetch['user'];
	
	if($case==0){
		$data = $conn->prepare("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas LEFT JOIN location on ideas.ididea=location.ididea WHERE country=:country 
		AND (SQRT(POW(RADIANS(:lat) - RADIANS(location.lat),2) + POW(((RADIANS(:long) - RADIANS(location.lng)) * (COS((RADIANS(:lat) + RADIANS(location.lat))/2))),2))*3959) < 25  ORDER BY time DESC;");
			$data->bindValue(':lat',$lat,PDO::PARAM_STR);
	$data->bindValue(':long',$long,PDO::PARAM_STR);
	}else if($case==1){
		$data = $conn->prepare("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas LEFT JOIN location on ideas.ididea=location.ididea WHERE country=:country 
		AND (SQRT(POW(RADIANS(:lat) - RADIANS(location.lat),2) + POW(((RADIANS(:long) - RADIANS(location.lng)) * (COS((RADIANS(:lat) + RADIANS(location.lat))/2))),2))*3959) < 100  ORDER BY time DESC;");
			$data->bindValue(':lat',$lat,PDO::PARAM_STR);
	$data->bindValue(':long',$long,PDO::PARAM_STR);
	}else if($case==2){
		$data = $conn->prepare("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas LEFT JOIN location on ideas.ididea=location.ididea WHERE country=:country AND state=:state ORDER BY time DESC");
		$data->bindValue(':state',$state,PDO::PARAM_STR);
	}else if($case==3){
		
		$data = $conn->prepare("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas LEFT JOIN location on ideas.ididea=location.ididea WHERE country=:country ORDER BY time DESC");
		
	}else if($case==4){
		$data = $conn->prepare("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ideas.ididea FROM ideas ORDER BY time DESC");
	}else if ($case==5){
		$data=$conn->prepare("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas LEFT JOIN friends ON ideas.iduser=friends.idfriend WHERE friends.iduser='$userid' ");
	}
	$data->bindValue(':country',$country,PDO::PARAM_STR);
	$data->setFetchMode(PDO::FETCH_ASSOC);
	$data->execute();
	$data_array=$data->fetchAll();
	echo "[";
	$count=count($data_array);
	for($i=0;$i<$count+1;$i++){
		if($i<$count){
		echo json_encode($data_array[$i]);
			echo",";
		}
	}
	echo "]";
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>