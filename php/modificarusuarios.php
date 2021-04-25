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
	echo 'Conexion establecida';
	$tipoFuncion = $_POST["funcion"];
		echo $tipoFuncion;

		if ($tipoFuncion=="pistas"){
			listarUsuarios($con);
			$nombreUsuario = $_POST["nombreUsuario"];
			$pistas= $_POST["pistas"];
			modificarPistas($con,$nombreUsuario,$pistas);
		}
	
	
	
}

function modificarPistas($con,$nombreUsuario,$pistas){
		echo 'Actualizando usuario';

			$resultado = mysqli_query($con, "UPDATE `Usuarios` SET pistas=$pistas WHERE nombreUsuario='$nombreUsuario'");

			if (!$resultado) {
				echo 'Ha ocurrido algún error: ' . mysqli_error($con);
			}
			else{
				echo "La puntuación se ha actualizado correctamente";
			}
}

?>