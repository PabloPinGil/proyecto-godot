extends CharacterBody2D


var wheel_base = 50 
var steering_angle = 20  
var steer_angle  
var engine_power = 1200  
var acceleration = Vector2.ZERO  
var friction = -0.9  
var drag = -0.0015  
var braking = -450 
var max_speed_reverse = 6000
var slip_speed = 800  
var traction_fast = 0.1  
var traction_slow = 0.7
var fire_rate = 0.25
var vida_max = 5
var vida = vida_max
@export var bala : PackedScene


func _physics_process(delta):
	acceleration = Vector2.ZERO
	get_input()
	apply_friction()
	calculate_steering(delta)
	velocity += acceleration * delta 
	
	handle_collision()
	move_and_slide()
	print(vida)
	
	if Input.is_action_pressed("shoot") and $shoot_timer.is_stopped():
		shoot()


func get_input():
	var turn = 0
	if Input.is_action_pressed("steer_right"):
		turn += 1
	if Input.is_action_pressed("steer_left"):
		turn -= 1	
	steer_angle = turn * deg_to_rad(steering_angle)
	
	if Input.is_action_pressed("accelerate"):
		acceleration = transform.x * engine_power
	if Input.is_action_pressed("brake"):
		acceleration = transform.x * braking


func apply_friction():
	if velocity.length() < 5:  # si el coche va muy lento lo detiene
		velocity = Vector2.ZERO
	
	var friction_force = velocity * friction  
	var drag_force = velocity * velocity.length() * drag  
	
	if velocity.length() < 100:
		friction_force *= 0.1  # Reduce el factor de amplificación
	else:
		friction_force *= 3
	
	acceleration += drag_force + friction_force  #aplica la fricción y el arrastre


func calculate_steering(delta):
	var rear_wheel = position - transform.x * wheel_base / 2.0  # vector dirección de las ruedas traseras
	var front_wheel = position + transform.x * wheel_base / 2.0  # vector dirección de las ruedas delanteras
	
	rear_wheel += velocity * delta  # aplica a los vectores de antes la velocidad de cada par de ruedas
	front_wheel += velocity.rotated(steer_angle) * delta
	
	var new_heading = (front_wheel - rear_wheel).normalized()  # calcula la dirección en base a la diferencia de los vectores
	
	var traction = traction_slow
	if velocity.length() > slip_speed:  # disminuye la tracción si se supera cierta velocidad
		traction = traction_fast
	
	var d = new_heading.dot(velocity.normalized())
	if d > 0:
		velocity = velocity.lerp(new_heading * velocity.length(), traction)
	if d < 0:
		velocity = -new_heading * min(velocity.length(), max_speed_reverse)
	rotation = new_heading.angle()


func handle_collision():
	for i in range(get_slide_collision_count()):
		var collision = get_slide_collision(i)
		if collision:
			var normal = collision.get_normal()
			
			# Ajusta el vector de velocidad para alejarse del muro
			velocity = velocity.slide(normal).normalized() * velocity.length() * 0.9


func shoot():
	$disparo.play()
	# Crear la bala
	var b = bala.instantiate()
	owner.add_child(b)
	b.transform = $Marker2D.global_transform
	# Esperar el tiempo de recarga antes de permitir disparar de nuevo
	$shoot_timer.start(fire_rate)


func curar():
	vida += 1
	if vida > vida_max:
		vida = vida_max


func potenciador():
	fire_rate = 0.4
	await get_tree().create_timer(3.0).timeout
	fire_rate = 0.25


func get_hit():
	vida -= 1
	if vida <= 0:
		queue_free()
	await get_tree().create_timer(3.0).timeout
