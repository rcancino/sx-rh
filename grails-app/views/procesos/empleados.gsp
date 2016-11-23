<%@page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="layout" content="operaciones2"/>
	<title>Empleados</title>
	
</head>
<body>
	<content tag="header">
		<h2>Empleados activos</h2>
	</content>

         <content tag="buttonsBar">
             <lx:refreshButton action="empleados"/>
          
         </content>
        
	<content tag="gridPanel" >
		<g:render template="empleadosGrid"/>
	</content>
</body>
</html>