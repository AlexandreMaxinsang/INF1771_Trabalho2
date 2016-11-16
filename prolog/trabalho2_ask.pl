%-------------------------------------------------------------------------------
% Ask_KB
%-------------------------------------------------------------------------------

ask_KB(Action) :- act(Action),!.

% Strategy Reflex

act(teletransport) :-
	agent_goal(find_out),
	agent_location(L),	
	teletransport_location(L),
	!.	
	
act(grab) :-
	agent_goal(find_out),
	agent_location(L),		
	is_gold(L),	
	!.	
	
act(grab) :-
	agent_goal(find_out),
	agent_location(L),
	agent_healthy(X),
	inf_equal(X,50),
	is_powerup(L),	
	!.	

	
act(die) :- 
	agent_goal(find_out),
	agent_location(L),
	enemy_location(_,_,L),
	agent_healthy(X),
	inf_equal(X,0),
	!.
	
act(die) :- 
	agent_goal(find_out),
	agent_location(L),
	pit_location(L),
	!.
		
act(shoot) :-
	agent_goal(find_out),
	agent_location(L),
	enemy_location(A,_,L),
	agent_healthy(X),
	no(inf_equal(X,A)),
	no(inf_equal(X,0)),
	agent_arrows(Y),
	no(inf_equal(Y,0)),
	!.	
	
act(climb) :-		% climb with gold
	agent_goal(go_out),
	agent_location([1,1]),	
	format("I'm going out~n",[]),		
	!.

act(astar_powerup):-
	agent_goal(find_out),
	is_powerup(_),
	agent_healthy(X),
	inf_equal(X,50),
	!.


act(forward) :-		
	agent_goal(find_out),
	location_ahead(L),		% this somewhere is just
	good(L),				% the room in front of me.
	no(is_wall(L)),
	!.
		
act(astar_saferoom):-
	agent_goal(find_out),
	is_saferoom(_),
	!.


	

act(astar_teletransport):-
	agent_goal(find_out),
	is_teletransport(_),
	agent_healthy(X),
	inf_equal(X,30),
	!.
	
act(astar_teletransport):-
	agent_goal(find_out),
	is_teletransport(_),
	agent_arrows(Y),
	inf_equal(Y,0),
	!.
	
act(astar_enemy):-
	agent_goal(find_out),
	is_enemy(_),
	!.
		
act(astar_gohome):-				% back to home with life 	
	agent_goal(go_out),
	!.	
