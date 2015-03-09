<?php
try{
	$conn = new PDO('mysql:host=localhost;dbname=mydb','root','CENSORED');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	echo "Connected Successfully";
	$cat=$_GET["cat"];
	$sub=$_GET["sub"];
	$cat = stripslashes($cat);
	$sub = stripslashes($sub);
	echo "<br>cat is: " . $cat . " sub is: " . $sub . "<br>";
	$data = $conn->query("SELECT title,author,descrip,time,thumbsup,thumbsdown,cat,sub FROM ideas WHERE cat='$cat' AND sub='$sub' ORDER BY ididea DESC");
	$data->setFetchMode(PDO::FETCH_ASSOC);
	foreach($data as $row){
		$r[]=json_encode($row);
	}
} catch(PDOException $e) {
	echo 'ERROR: ' . $e->getMessage();
}
?>
<script type="text/javascript">
var jArray = <?php echo json_encode($r); ?>;
function show(j) {
    for(var i=0; i < j.length; i++){ document.write(j[i]); }
};
show(jArray);
</script>