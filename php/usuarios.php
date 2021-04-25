<?php
$DB_SERVER="localhost"; #la dirección del servidor
$DB_USER="Xnlebena001"; #el usuario para esa base de datos
$DB_PASS="0xzLJgFPqz"; #la clave para ese usuario
$DB_DATABASE="Xnlebena001_MessyWords";

$con = mysqli_connect($DB_SERVER, $DB_USER, $DB_PASS, $DB_DATABASE);

if (mysqli_connect_errno($con)) {
echo 'Error de conexion: ' . mysqli_connect_error();
exit();
}
else{
		$tipoFuncion = $_POST["funcion"];
		if ($tipoFuncion=="listar"){
			listarUsuarios($con);
		}
		else if($tipoFuncion=="datosUsuario"){
			$nombreUsuario = $_POST["nombreUsuario"];

			datosUsuario($con,$nombreUsuario);
		}
		else if($tipoFuncion=="insertarUsuario"){
			echo "Insertar usuario";
			$nombreUsuario = $_POST["nombreUsuario"];
			$email = $_POST["email"];
			$contrasena= $_POST["contrasena"];
			$contrasena=password_hash($contrasena, PASSWORD_DEFAULT);

			insertarUsuario($con,$nombreUsuario,$email,$contrasena);
		}
		else if($tipoFuncion=="comprobarContrasena"){
			$nombreUsuario = $_POST["nombreUsuario"];
			$contrasena= $_POST["contrasena"];
			comprobarContrasena($con,$nombreUsuario,$contrasena);
		}
		else if($tipoFuncion=="obtenerImagen"){
			echo "obteniendo imagen";
			$nombreUsuario = $_POST["nombreUsuario"];
			getFoto($nombreUsuario,$con);
		}
		else if ($tipoFuncion=="pistas"){
			listarUsuarios($con);
			echo $tipoFuncion;
			$nombreUsuario = $_POST["nombreUsuario"];
			$pistas= $_POST["pistas"];
			modificarPistas($con,$nombreUsuario,$pistas);
		}
		else if ($tipoFuncion=="puntuacion"){
			listarUsuarios($con);
			echo $tipoFuncion;
			$nombreUsuario = $_POST["nombreUsuario"];
			$pistas= $_POST["puntuacion"];
			modificarPuntuacion($con,$nombreUsuario,$pistas);
		}
		

}

function listarUsuarios($con){
		$resultado = mysqli_query($con, "SELECT * FROM Usuarios ORDER BY Puntuacion DESC");
	if (!$resultado) {
		echo 'Ha ocurrido algún error: ' . mysqli_error($con);
	}
	else{
		
		$arrayresultados= array();

		 while ($fila = $resultado->fetch_row()) {

			# Generar el array con los resultados con la forma Atributo - Valor
			array_push($arrayresultados, array(
			'NombreUsuario' => $fila[0],
			'Email' => $fila[1],
			'Contrasena' => $fila[2],
			'Puntuacion' => $fila[3]
			));
			
		}
					echo json_encode($arrayresultados);
	}
}

function datosUsuario($con,$nombreUsuario){
		$resultado = mysqli_query($con, "SELECT `NombreUsuario`, `Email`,`Puntuacion`,`Pistas` FROM Usuarios WHERE NombreUsuario = '$nombreUsuario'");
		if (!$resultado) {
			echo 'Ha ocurrido algún error: ' . mysqli_error($con);
		}
		else{
			#Acceder al resultado
			$fila = mysqli_fetch_row($resultado);
			
			# Generar el array con los resultados con la forma Atributo - Valor
			$arrayresultados = array(
			'NombreUsuario' => $fila[0],
			'Email' => $fila[1],
			'Puntuacion' => $fila[2],
			'Pistas' => $fila[3],
			);
			echo json_encode($arrayresultados);
		}
}

function insertarUsuario($con,$nombreUsuario,$email,$contrasena){
		$linkFoto="$nombreUsuario.jpg";
		#subirFoto($linkFoto,$foto);
		$resultado = mysqli_query($con, "INSERT INTO `Usuarios` (`NombreUsuario`, `Email`, `Contrasena`, `Puntuacion`, `Pistas`) VALUES ('$nombreUsuario', '$email', '$contrasena', '0', '0')");
		if (!$resultado) {
			echo 'Ha ocurrido algún error: ' . mysqli_error($con);
		}
		else{
			echo 'Se ha insertado el usuario correctamente';
		}
}

function comprobarContrasena($con,$nombreUsuario,$contrasena){
	$resultado = mysqli_query($con, "SELECT Contrasena FROM Usuarios WHERE NombreUsuario = '$nombreUsuario'");
	if (!$resultado) {
		echo 'Ha ocurrido algún error: ' . mysqli_error($con);
	}
	else{
		 $hash="";
		 while ($fila = $resultado->fetch_row()) {
			 $hash=$fila[0];
		}
		if (password_verify($contrasena , $hash)) {
			echo true;
		} else {
			echo 0;
		}
	 	
	}
}

function subirFoto($path,$foto){
	$binary=base64_decode($foto);
	header('Content-Type: bitmap; charset=utf-8');
	$file = fopen($path, 'w+');
	file_put_contents("http://ec2-54-167-31-169.compute-1.amazonaws.com/nlebena001/WEB/imagenesUsuarios/imagen.jpg", $binary);
}

function getFoto($nombreUsuario,$con){
	$query = "SELECT * FROM Imagen where NombreUsuario='$nombreUsuario'";
	$result = mysqli_query($con,$query);
	if (!$result) {
		echo 'Ha ocurrido algún error: ' . mysqli_error($con);
		exit;
	}
	else {
		$photo = mysqli_fetch_array($result);
		echo base64_decode($photo['foto']);
	}
	
}
function modificarPistas($con,$nombreUsuario,$pistas){
		echo 'Actualizando usuario';

			$resultado = mysqli_query($con, "UPDATE `Usuarios` SET pistas='$pistas' WHERE nombreUsuario='$nombreUsuario'");

			if (!$resultado) {
				echo 'Ha ocurrido algún error: ' . mysqli_error($con);
			}
			else{
				echo "La puntuación se ha actualizado correctamente";
			}
}

function modificarPuntuacion($con,$nombreUsuario,$puntuacion){
		echo 'Actualizando usuario';

			$resultado = mysqli_query($con, "UPDATE `Usuarios` SET puntuacion=puntuacion+'$puntuacion' WHERE nombreUsuario='$nombreUsuario'");

			if (!$resultado) {
				echo 'Ha ocurrido algún error: ' . mysqli_error($con);
			}
			else{
				echo "La puntuación se ha actualizado correctamente";
			}
}



?>