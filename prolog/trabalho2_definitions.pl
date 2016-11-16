%-------------------------------------------------------------------------------
% More Definitions and Axioms
%
:- dynamic([
	nb_gold/1,
	agent_goal/1,
	powerup_location/1,
	enemy_location/3,
	gold_location/1,
	teletransport_location/1,
	pit_location/1,
	score_powerup/1,
	score_grab/1,
	score_agent_dead/1,
	land_extent/1,
	agent_location/1,		% state of agent
	agent_orientation/1,
	agent_healthy/1,
	agent_hold/1,
	agent_arrows/1,
	agent_score/1,
	agent_in_cave/0,
	has_enemy/1,
	has_pit/1,
	has_teletransport/1,
	is_saferoom /1,			% agent's knowledge possible about saferoom_location
	is_powerup /1,			% agent's knowledge possible about powerup_location
	is_enemy /1,				% agent's knowledge possible about enemy_location
	is_pit /1,			% agent's knowledge possible about pit_location
	is_gold /1,			% agent's knowledge possible about gold_location
	is_wall /1,			% agent's knowledge possible about wall_location
	is_teletransport/1,	% agent's knowledge possible about teletransport_location
	is_visited/1		% agent's knowledge possible about room visited
		]).		

	
initialize:-
		retractall(nb_gold(_)),
		retractall(is_teletransport(_)),
		retractall(is_saferoom(_)),
		retractall(is_enemy(_)),
		retractall(is_pit(_)),
		retractall(is_gold(_)),
		retractall(is_wall(_)),
		retractall(is_visited(_)),
		retractall(land_extent(_)),
		retractall(agent_goal(_)),
		retractall(agent_orientation(_)),
		retractall(agent_location(_)),
		retractall(agent_healthy(_)),
		retractall(score_agent_dead(_)),
		retractall(agent_arrows(_)),
		retractall(agent_hold(_)),
		retractall(score_grab(_)),
		retractall(score_powerup(_)),
		retractall(agent_score(_)),
		retractall(agent_in_cave),
		
	
		assert(nb_gold(3)),
		assert(agent_goal(find_out)),
		assert(score_powerup(20)),
		assert(is_visited([1,1])),
		assert(land_extent(13)),
		assert(agent_in_cave),
		assert(agent_score(0)),
		assert(agent_orientation([1,0])),	
		assert(agent_location([1,1])),
		assert(agent_healthy(100)),
		assert(score_grab(1000)),
		assert(score_agent_dead(1000)),
		assert(agent_arrows(5)),
		assert(agent_hold(0)),
		!.

no(P) :- 
	P,
	!,
	fail. 
no(P).	

location_toward([X,Y],[1,0],[New_X,Y]) :- New_X is X+1 .
location_toward([X,Y],[0,1],[X,New_Y]) :- New_Y is Y+1 .
location_toward([X,Y],[-1,0],[New_X,Y]) :- New_X is X-1 .
location_toward([X,Y],[0,-1],[X,New_Y]) :- New_Y is Y-1 .

diagonal([X,Y],[XBL,YBL],[XBR,YBR],[XTL,YTL],[XTR,YTR]):- 
	XBL is (X - 1),
	YBL is (Y - 1),
	XBR is (X + 1),
	YBR is (Y - 1),
	XTL is (X - 1),
	YTL is (Y + 1),
	XTR is (X + 1),
	YTR is (Y + 1),
	!.

adjacent(L1,L2) :- location_toward(L1,_,L2).

location_ahead(Ahead) :-
	agent_location(L),
	agent_orientation(O),
	location_toward(L,O,Ahead),
	!.

inf_equal(X,Y) :- X < Y,!.
inf_equal(X,Y) :- X = Y.

dist(X,Y,R) :- 
	inf_equal(X,Y),	
	R is Y - X,
	!.
dist(X,Y,R) :- R is X - Y.

wall_location([X,LE]) :- inf_equal(LE,0).		% there is wall
wall_location([LE,Y]) :- inf_equal(LE,0).		% there is wall
wall_location([X,Y]) :- land_extent(LE), inf_equal(LE,X).% there is wall
wall_location([X,Y]) :- land_extent(LE), inf_equal(LE,Y).% there is wall



% A location is estimated thanks to ... good, medium, risky, deadly.	

good(L) :-				% a wall can be a good room !!!
    no(wall_location(L)),
    no(is_visited(L)),
	no(has_enemy(L)),
	no(has_pit(L)),
	no(has_teletransport(L)),
	no(is_enemy(L)),
	no(is_pit(L)),
	no(is_teletransport(L)),
	!.
	
