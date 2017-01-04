<ul class="nav navbar-top-links navbar-right">
    <li>
        <g:link controller="home">
            <span class="m-r-sm text-muted welcome-message">SX-RH (${session?.empresa?.nombre}) Ejercicio: ${session?.ejercicio}
        </span>
        </g:link>
        
    </li>

    <li>
        <a href="" data-toggle="modal" data-target="#cambioGlobalDeEjercicioForm">
            <i class="fa fa-calendar"></i>
        </a>
    </li>

    <li>
        <a data-target="#empleadoSearchDialog" data-toggle="modal">
            <i class="fa fa-user"></i> 
        </a>
    </li>
    <li class="dropdown">
        
        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
            <i class="fa fa-envelope"></i>  <span class="label label-warning"></span>
        </a>
        
        <ul class="dropdown-menu dropdown-messages">
            
            
            <li>
                No hay mensajes pendientes
            </li>
            <li class="divider"></li>
            <li>
                <div class="text-center link-block">
                    <a href="mailbox.html">
                        <i class="fa fa-envelope"></i> <strong>Ver todos los mensajes</strong>
                    </a>
                </div>
            </li>
        </ul>
    </li>
    <li class="dropdown">
        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
            <i class="fa fa-bell"></i>  <span class="label label-primary"></span>
        </a>
        <ul class="dropdown-menu dropdown-alerts">
            <li>
                No hay alertas en este momento  
            </li>
            <li class="divider"></li>   
            <li>
                <div class="text-center link-block">
                    <a href="notifications.html">
                        <strong>Ver todas las alertas</strong>
                        <i class="fa fa-angle-right"></i>
                    </a>
                </div>
            </li>
        </ul>
    </li>

    
    <li>
        <g:form controller="logout" class="" >
            <button class="btn btn-w-m btn-link" type="submit" > <i class="fa fa-sign-out"></i></i> Cerrar sesión</button>
        </g:form>
        
    </li>
    <li>
        <a class="right-sidebar-toggle">
            <i class="fa fa-tasks"></i>
        </a>
    </li>
</ul>