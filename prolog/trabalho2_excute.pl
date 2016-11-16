%-------------------------------------------------------------------------------
% carry out : result(Action)
%

teletransport :-
	land_extent(LE),
	New_LE is LE -1,
	random(1,New_LE,X),
	random(1,New_LE,Y),
	retractall(agent_location(_)),
	assert(agent_location([X,Y])),
	!.
		
die :-
	agent_location(L),
	enemy_location(_,_,L),			
	retractall(is_enemy(L)),	
	assert(is_enemy(L)),		
	agent_score(S),
	score_agent_dead(SAD),
	New_S is (S - SAD),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	retractall(agent_healthy(_)),
	assert(agent_healthy(0)),
	retractall(agent_in_cave),
	!.
		
die :-	
	agent_location(L),
	pit_location(L),			
	retractall(is_pit(L)),		
	assert(is_pit(L)),			
	agent_score(S),
	score_agent_dead(SAD),
	New_S is (S - SAD),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	retractall(agent_healthy(_)),
	assert(agent_healthy(0)),
	retractall(agent_in_cave),
	!.
	
	
grab :-
	agent_location(L),
	is_gold(L),
	retractall(gold_location(L)),	% no more gold at this place
	retractall(is_gold(L)),			% The gold is with me!
	agent_hold(X),
	New_X is (X+1),
	retractall(agent_hold(_)),
	assert(agent_hold(New_X)),		% money, money,  :P 
	agent_score(S),
	score_grab(SG),
	New_S is (S + SG -1),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	update_agent_goal,
	!.

grab :-
	agent_location(L),
	is_powerup(L),
	agent_healthy(H),
	score_powerup(SG),
	New_H is (H + SG),
	retractall(agent_healthy(_)),
	assert(agent_healthy(New_H)),
	retractall(powerup_location(L)),	% no more powerup in L
	retractall(is_powerup(L)),
	agent_score(S),
	New_S is (S - 1),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	!.

	

rebound :-
	agent_location(L),
	agent_orientation([X,Y]),		
	New_X is -1*X,
	New_Y is -1*Y,
	location_toward(L,[New_X,New_Y],L2),
	retractall(agent_orientation(_)),
	assert(agent_orientation([New_X,New_Y])),	
	retractall(agent_location(_)),
	assert(agent_location(L2)),			% back at the last location
	agent_score(S),
	New_S is (S - 1),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	!.		
	
	
climb :-	
	retractall(agent_in_cave),
	agent_score(S),
	New_S is (S - 1),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	!.	
	
	
forward :-
	agent_orientation(O),
	agent_location(L),
	location_toward(L,O,New_L),
	retractall(agent_location(_)),
	assert(agent_location(New_L)),
	update_state(New_L),
	agent_score(S),
	New_S is (S - 1),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	!.
	
turn([Xf,Yf]) :-	
	agent_location([X0,Y0]),			% turn 90 degree counter-clockwise.
	agent_orientation([X1,Y1]),     
	New_X is (Xf-X0),
	New_Y is (Yf-Y0),
	New_X =:=  -Y1,
	New_Y =:=  X1,
	retractall(agent_orientation(_)),
	assert(agent_orientation([New_X,New_Y])),
	agent_score(S),
	New_S is (S - 3),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	!.
	
turn([Xf,Yf]) :-	
	agent_location([X0,Y0]),			% turn 90 degree clockwise.
	agent_orientation([X1,Y1]),     
	New_X is (Xf-X0),
	New_Y is (Yf-Y0),
	New_X =:= Y1,
	New_Y =:= -X1,
	retractall(agent_orientation(_)),
	assert(agent_orientation([New_X,New_Y])),
	agent_score(S),
	New_S is (S - 1),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	!.
turn([Xf,Yf]) :-	
	agent_location([X0,Y0]),			% turn 180 degree clockwise.
	agent_orientation([X1,Y1]),     
	New_X is (Xf-X0),
	New_Y is (Yf-Y0),
	New_X =:= -X1,
	New_Y =:= -Y1,
	retractall(agent_orientation(_)),
	assert(agent_orientation([New_X,New_Y])),
	agent_score(S),
	New_S is (S - 2),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	!.	


shoot :-						
	agent_location(L),
	enemy_location(A,H,L),	
	agent_arrows(X),
	New_X is (X-1),
	retractall(agent_arrows(_)),
	assert(agent_arrows(New_X)),
	agent_healthy(Y),
	New_Y is (Y - A),
	retractall(agent_healthy(_)),
	assert(agent_healthy(New_Y)),
	agent_score(S),
	New_S is (S -(A+10)),
	retractall(agent_score(_)),
	assert(agent_score(New_S)),
	random(20,50,D),
	New_H is (H - D),
	update_enemy(A,New_H,L),
	!.
	

%  auxiliary rules

update_agent_goal :-
		agent_hold(N),
		nb_gold(X),
		N =:= X,
		retractall(agent_goal(_)),
		assert(agent_goal(go_out)),
		!.
update_agent_goal.	

update_state(L) :-
	no(enemy_location(_,_,L)),
	no(pit_location(L)),
	no(teletransport_location(L)),
	retractall(is_teletransport(L)),
	retractall(is_pit(L)),
	retractall(is_enemy(L)),
	retractall(is_saferoom(L)),
	retractall(is_visited(L)),
	assert(is_visited(L)),
	!.
update_state(L) :-
	enemy_location(_,_,L),
	retractall(is_teletransport(L)),
	retractall(is_pit(L)),
	retractall(is_enemy(L)),
	retractall(is_saferoom(L)),
	retractall(is_visited(L)),
	assert(has_enemy(L)),
	!.
update_state(L) :-
	pit_location(L),
	retractall(is_teletransport(L)),
	retractall(is_pit(L)),
	retractall(is_enemy(L)),
	retractall(is_saferoom(L)),
	retractall(is_visited(L)),
	assert(has_pit(L)),
	!.
update_state(L) :-
	teletransport_location(L),
	retractall(is_teletransport(L)),
	retractall(is_pit(L)),
	retractall(is_enemy(L)),
	retractall(is_saferoom(L)),
	retractall(is_visited(L)),
	assert(has_teletransport(L)),
	!.
update_state(L) :-
	wall_location(L),
	assert(is_wall(L)),
	!.
update_state(L).
	
	
update_enemy(Kind,Healthy,Location):-
		inf_equal(Healthy,0),
		retractall(enemy_location(Kind,_,Location)),
		!.
	
update_enemy(Kind,Healthy,Location):-
		retractall(enemy_location(Kind,_,Location)),
		assert(enemy_location(Kind,Healthy,Location)),
		!.