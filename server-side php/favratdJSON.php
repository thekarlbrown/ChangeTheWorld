<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$username=$_GET["username"];
	$ideaid=$_GET["ideaid"];
	$case=$_GET["case"];

	$stmt=$conn->prepare("SELECT user FROM user WHERE author=?");
	$stmt->execute(array($username));
	$fetch=$stmt->fetch(PDO::FETCH_ASSOC);
	$userid=$fetch['user'];

	$stmt=$conn->prepare("INSERT IGNORE INTO favratd (iduser, ididea) VALUES (:userid,:ideaid)");
	$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
	$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
	if($stmt->execute()){
			echo "[{\"added\":\"1\"}]";
		}else{
			echo "[{\"added\":\"0\"}]";
		}

	if($case==0){
		$stmt=$conn->prepare("UPDATE favratd set fav='0' WHERE iduser=:userid AND ididea=:ideaid");
		$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
		$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
		$stmt->execute();
	}else if($case==1){
		$stmt=$conn->prepare("UPDATE favratd set fav='1' WHERE iduser=:userid AND ididea=:ideaid");
		$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
		$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
		$stmt->execute();
	}else if($case==2){
		$stmt=$conn->prepare("SELECT rated FROM favratd WHERE iduser=:userid AND ididea=:ideaid");
		$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
		$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
		$stmt->execute();
		$fetch=$stmt->fetch(PDO::FETCH_ASSOC);
		$lastrated=$fetch['rated'];
		//echo $lastrated;
		$stmt=$conn->prepare("UPDATE favratd set rated=NULL WHERE iduser=:userid AND ididea=:ideaid");
		$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
		$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
		if($stmt->execute()){
			if($lastrated==NULL){
			}else if($lastrated==0){
				$stmt=$conn->prepare("UPDATE ideas set thumbsdown=thumbsdown-1 WHERE ididea=:ideaid");
				$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
				$stmt->execute();
			}else if($lastrated==1){
				$stmt=$conn->prepare("UPDATE ideas set thumbsup=thumbsup-1 WHERE ididea=:ideaid");
				$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
				$stmt->execute();
			}
		}
	}else if($case==3){
		$stmt=$conn->prepare("SELECT rated FROM favratd WHERE iduser=:userid AND ididea=:ideaid");
		$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
		$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
		$stmt->execute();
		$fetch=$stmt->fetch(PDO::FETCH_ASSOC);
		$lastrated=$fetch['rated'];
		//echo $lastrated;
		$stmt=$conn->prepare("UPDATE favratd set rated='1' WHERE iduser=:userid AND ididea=:ideaid");
		$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
		$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
		if($stmt->execute()){
			if($lastrated==NULL){
				$stmt=$conn->prepare("UPDATE ideas set thumbsup=thumbsup+1 WHERE ididea=:ideaid");
				$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
				$stmt->execute();
			}else if($lastrated==0){
				$stmt=$conn->prepare("UPDATE ideas set thumbsdown=thumbsdown-1 WHERE ididea=:ideaid");
				$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
				$stmt->execute();
				$stmt=$conn->prepare("UPDATE ideas set thumbsup=thumbsup+1 WHERE ididea=:ideaid");
				$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
				$stmt->execute();
			}else if($lastrated==1){
			}
		}
	}else if($case==4){
		$stmt=$conn->prepare("SELECT rated FROM favratd WHERE iduser=:userid AND ididea=:ideaid");
		$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
		$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
		$stmt->execute();
		$fetch=$stmt->fetch(PDO::FETCH_ASSOC);
		$lastrated=$fetch['rated'];
		//echo $lastrated;
		$stmt=$conn->prepare("UPDATE favratd set rated='0' WHERE iduser=:userid AND ididea=:ideaid");
		$stmt->bindValue(':userid',$userid,PDO::PARAM_INT);
		$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
		if($stmt->execute()){
			if($lastrated==NULL){
				$stmt=$conn->prepare("UPDATE ideas set thumbsdown=thumbsdown+1 WHERE ididea=:ideaid");
				$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
				$stmt->execute();
			}else if($lastrated==0){
			}else if($lastrated==1){
				$stmt=$conn->prepare("UPDATE ideas set thumbsdown=thumbsdown+1 WHERE ididea=:ideaid");
				$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
				$stmt->execute();
				$stmt=$conn->prepare("UPDATE ideas set thumbsup=thumbsup-1 WHERE ididea=:ideaid");
				$stmt->bindValue(':ideaid',$ideaid,PDO::PARAM_INT);
				$stmt->execute();
			}
		}
	}	

} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>