<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$cat=$_GET["cat"];
	$sub=$_GET["sub"];
	$case=$_GET["case"];
	$cat = stripslashes($cat);
	$sub = stripslashes($sub);
	$case = stripslashes($case);
	if($case==0){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas WHERE cat='$cat' AND sub='$sub' AND time BETWEEN NOW() - INTERVAL 1 DAY AND NOW() ORDER BY ididea DESC");
	}else if($case==1){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas WHERE cat='$cat' AND sub='$sub' AND time BETWEEN NOW() - INTERVAL 7 DAY AND NOW() ORDER BY ididea DESC");
	}else if($case==2){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas WHERE cat='$cat' AND sub='$sub' AND time BETWEEN NOW() - INTERVAL 30 DAY AND NOW() ORDER BY ididea DESC");
	}else if($case==3){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas WHERE cat='$cat' AND sub='$sub' AND time BETWEEN NOW() - INTERVAL 365 DAY AND NOW() ORDER BY ididea DESC");
	}else if($case==4){
		$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub,ididea FROM ideas WHERE cat='$cat' AND sub='$sub' ORDER BY ididea DESC");
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