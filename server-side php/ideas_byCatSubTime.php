<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	echo "Connected Successfully";
	$cat=$_GET["cat"];
	$sub=$_GET["sub"];
	$case=$_GET["case"];
	$cat = stripslashes($cat);
	$sub = stripslashes($sub);
	$case = stripslashes($case);
	echo "<br>cat is: " . $cat . " sub is: " . $sub . " case is " . $case . "<br>";
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
	foreach($data as $row){
		echo json_encode($row);
	}
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>