package lando.systems.ld35.utils;

import com.badlogic.gdx.math.Vector2;

public enum Level {

    // REMOVE: 13, 23, 29,30,31,37,49,63,64

    INTRODUCTION                     ("maps/level-intro.tmx",                      32, 64, new boolean[] {true, false, false, false, false, false}),
    ROCKETANVIL_EASY                 ("maps/level-rocket-anvil-easy.tmx",          32, 64, new boolean[] {true,  true,  true, false, false, false}),
    ROCKETANVIL_MED                  ("maps/level-rocket-anvil-medium.tmx",        32, 64, new boolean[] {true,  true,  true, false, false, false}),
    TORUS_TUTORIAL                   ("maps/level-torus-tutorial.tmx",             32, 64, new boolean[] {true,  true,  true,  true, false, false}),
    TORUS_MED                        ("maps/level-torus-medium.tmx",               32, 64, new boolean[] {true,  true,  true,  true, false, false}),
    MAGNET_EASY                      ("maps/level-magnet-easy.tmx",                32, 64, new boolean[] {true,  true,  true,  true,  true, false}),
    ROPE_TUTORIAL                    ("maps/level-rope-tutorial.tmx",              32, 64, new boolean[] {true,  true,  true,  true,  true,  true}),
    GLORIOUS_TORUS                   ("maps/Glorious_Torus.tmx",                   32, 64, new boolean[] { true, true, true, true, true, true}),
    DOOR_TUTORIAL                    ("maps/level_door_tutorial.tmx",              32, 64, new boolean[] {true,  true,  true,  true,  true,  true}),
    DOOR_MED                         ("maps/level-branching-doors.tmx",            32, 64, new boolean[] {true,  true,  true,  true,  true,  true}),
    SEEMS_EASY_ENOUGH                ("maps/Seems_Easy_Enough.tmx",                32, 64, new boolean[] { true, true, true, true, true, true}),
    SEEMS_LESS_EASY                  ("maps/Seems_Less_Easy.tmx",                  32, 64, new boolean[] { true, true, true, true, true, true}),
//  MAGNET_TORUS_HARD                ("maps/level-magnet-torus-hard.tmx",          32, 64, new boolean[] {true,  true,  true,  true,  true,  true}),
    CHALLENGE_1                      ("maps/level-challenge-1.tmx",                32, 64, new boolean[] {true,  true,  true,  true,  true,  true}),
//    SPIKES_HARD                      ("maps/level-spikes-hard.tmx",                32, 64, new boolean[] {true,  true,  true,  true,  true,  true}),
//    ALLIGATOR                        ("maps/Alligator.tmx",                        32, 64, new boolean[] { true, true, true, true, true, true}),
    QUESTION_MARK                    ("maps/Question_Mark.tmx",                    32, 64, new boolean[] { true, true, true, true, true, true}),
    ROCKET                           ("maps/Rocket.tmx",                           32, 64, new boolean[] { true, true, true, true, true, true}),
    ROUND_AND_ROUND                  ("maps/Round_and_Round.tmx",                  32, 64, new boolean[] { true, true, true, true, true, true}),
    TAKE_IT_SLOW                     ("maps/Take_It_Slow.tmx",                     32, 64, new boolean[] { true, true, true, true, true, true}),
    TRAINING_WHEELS_ALMOST_OFF       ("maps/Training_Wheels_Almost_Off.tmx",         32, 64, new boolean[] { true, true, true, true, true, true}),


    DOWN_BELOW_THE_RECTANGLES        ("maps/Down_Below_The_Rectangles.tmx",        32, 64, new boolean[] { true, true, true, true, true, true}),
    EASY_PEEZY                       ("maps/Easy_Peezy.tmx",                       32, 64, new boolean[] { true, true, true, true, true, true}),
    GATES_EVERYWHERE                 ("maps/Gates_Everywhere.tmx",                 32, 64, new boolean[] { true, true, true, true, true, true}),
    FIGHT_THE_CURRENT                ("maps/Fight_The_Current.tmx",                32, 64, new boolean[] { true, true, true, true, true, true}),
    FIGHT_THE_CURRENT_2              ("maps/Fight_The_Current_2.tmx",              32, 64, new boolean[] { true, true, true, true, true, true}),
    FIGHT_THE_CURRENT_3              ("maps/Fight_The_Current_3.tmx",              32, 64, new boolean[] { true, true, true, true, true, true}),
//    FISH                             ("maps/Fish.tmx",                             32, 64, new boolean[] { true, true, true, true, true, true}),
//    FLOATING_PATTERNED_SPIKES        ("maps/Floating_Patterned_Spikes.tmx",        32, 64, new boolean[] { true, true, true, true, true, true}),
    INTO_THE_CENTER                  ("maps/Into_The_Center.tmx",                  32, 64, new boolean[] { true, true, true, true, true, true}),
    SPACIOUS                         ("maps/Spacious.tmx",                         32, 64, new boolean[] { true, true, true, true, true, true}),
    JUST_GO_RIGHT                    ("maps/Just_Go_Right.tmx",                    32, 64, new boolean[] { true, true, true, true, true, true}),
    JUST_GO_RIGHT_2                  ("maps/Just_Go_Right_2.tmx",                  32, 64, new boolean[] { true, true, true, true, true, true}),
    JUST_GO_RIGHT_3                  ("maps/Just_Go_Right_3.tmx",                  32, 64, new boolean[] { true, true, true, true, true, true}),
//    JUST_GO_RIGHT_4                  ("maps/Just_Go_Right_4.tmx",                  32, 64, new boolean[] { true, true, true, true, true, true}),
//    JUST_GO_RIGHT_4A                 ("maps/Just_Go_Right_4a.tmx",                 32, 64, new boolean[] { true, true, true, true, true, true}),
//    LEVEL3                           ("maps/level3.tmx",                           32, 64, new boolean[] { true, true, true, true, true, true}),
    MAGNET_TURNING_TUTORIAL          ("maps/Magnet_Turning_Tutorial.tmx",          32, 64, new boolean[] { true, true, true, true, true, true}),
    MAGNET_SLOLEM_LEARN_TO_TURN      ("maps/Magnet_Slolem_Learn_to_Turn.tmx",      32, 64, new boolean[] { true, true, true, true, true, true}),
    MAGNET_SLOLEM_DIFFICULTY_LEVEL_2 ("maps/Magnet_Slolem_Difficulty_Level_2.tmx", 32, 64, new boolean[] { true, true, true, true, true, true}),
    MAGNET_SLOLEM_DIFFICULTY_LEVEL_3 ("maps/Magnet_Slolem_Difficulty_Level_3.tmx", 32, 64, new boolean[] { true, true, true, true, true, true}),
    CUT_AND_COURSE_CORRECT           ("maps/Cut_and_Course_Correct.tmx",           32, 64, new boolean[] { true, true, true, true, true, true}),
    CUT_ROPES_KEEP_MOVING            ("maps/Cut_Ropes_Keep_Moving.tmx",            32, 64, new boolean[] { true, true, true, true, true, true}),
//    MAZE1                            ("maps/Maze1.tmx",                            32, 64, new boolean[] { true, true, true, true, true, true}),
//    MAZE2                            ("maps/Maze2.tmx",                            32, 64, new boolean[] { true, true, true, true, true, true}),
    MAZE3                            ("maps/Maze3.tmx",                            32, 64, new boolean[] { true, true, true, true, true, true}),
    MAZE4                            ("maps/Maze4.tmx",                            32, 64, new boolean[] { true, true, true, true, true, true}),
    MAZE5                            ("maps/Maze5.tmx",                            32, 64, new boolean[] { true, true, true, true, true, true}),
    MAZE6                            ("maps/Maze6.tmx",                            32, 64, new boolean[] { true, true, true, true, true, true}),
    CLOGGED_ON_BOTH_SIDES            ("maps/Clogged_On_Both_Sides.tmx",            32, 64, new boolean[] { true, true, true, true, true, true}),
    PIZZA_SLICE                      ("maps/Pizza_Slice.tmx",                      32, 64, new boolean[] { true, true, true, true, true, true}),
    MORE_OF_THE_SAME                 ("maps/More_of_the_Same.tmx",                 32, 64, new boolean[] { true, true, true, true, true, true}),
    TREE                             ("maps/Tree.tmx",                             32, 64, new boolean[] { true, true, true, true, true, true}),
    RABBIT                           ("maps/Rabbit.tmx",                           32, 64, new boolean[] { true, true, true, true, true, true}),
    CUT_THE_CORRECT_ROPES            ("maps/Cut_The_Correct_Ropes.tmx",            32, 64, new boolean[] { true, true, true, true, true, true}),
    UP_AROUND_AND_BACK_AGAIN         ("maps/Up_Around_and_Back_Again.tmx",         32, 64, new boolean[] { true, true, true, true, true, true}),
    THREE_WIND_TUNNEL_BLOCKAGES      ("maps/Three_Wind_Tunnel_Blockages.tmx",      32, 64, new boolean[] { true, true, true, true, true, true}),
    SIMPLE_AND_SPACIOUS              ("maps/Simple_and_Spacious.tmx",              32, 64, new boolean[] { true, true, true, true, true, true}),
    SLOLEM                           ("maps/Slolem.tmx",                           32, 64, new boolean[] { true, true, true, true, true, true}),
//    SNAKE                            ("maps/Snake.tmx",                            32, 64, new boolean[] { true, true, true, true, true, true}),
    SOME_CLEVER_NAME                 ("maps/Some_Clever_Name.tmx",                 32, 64, new boolean[] { true, true, true, true, true, true}),

    TEST_OF_SKILLS                   ("maps/Test_of_Skills.tmx",                   32, 64, new boolean[] { true, true, true, true, true, true}),
    THIS_SHOULD_BE_TOUGH             ("maps/This_Should_Be_Tough.tmx",             32, 64, new boolean[] { true, true, true, true, true, true}),
    THREE_SIMPLE_TRAPS               ("maps/Three_Simple_Traps.tmx",               32, 64, new boolean[] { true, true, true, true, true, true}),

    TOP_OR_BOTTOM_PATH               ("maps/Top_or_Bottom_Path.tmx",               32, 64, new boolean[] { true, true, true, true, true, true}),
    TOTALLY_NECESSARY_ROPES          ("maps/Totally_Necessary_Ropes.tmx",          32, 64, new boolean[] { true, true, true, true, true, true}),


    TROY_IS_SUPER_PROUD_OF_THIS_ONE  ("maps/Troy_Is_Super_Proud_of_This_One.tmx",  32, 64, new boolean[] { true, true, true, true, true, true}),
    UNNECESSARY_ROPES_               ("maps/Unnecessary_Ropes.tmx",                32, 64, new boolean[] { true, true, true, true, true, true}),

    JANUARY_NEW_LEVEL_1              ("maps/January_New_Level_1.tmx",              32, 64, new boolean[] { true, true, true, true, true, true}),
    JANUARY_NEW_LEVEL_2              ("maps/January_New_Level_2.tmx",              32, 64, new boolean[] { true, true, true, true, true, true}),
    //    WIDE_PATHWAYS_AND_LONG_ROPES     ("maps/Wide_Pathways_and_Long_Ropes.tmx",     32, 64, new boolean[] { true, true, true, true, true, true}),

    ANOTHER_MARCH_LEVEL              ("maps/Another_March_Level.tmx",              32, 64, new boolean[] { true, true, true, true, true, true}),
    DIAGONAL_SKILLS_TEST             ("maps/Diagonal_Skills_Test.tmx",             32, 64, new boolean[] { true, true, true, true, true, true}),
    FIGURE_8                         ("maps/Figure_8.tmx",                         32, 64, new boolean[] { true, true, true, true, true, true}),

    PASTELS_WAGER                    ("maps/Pastels_Wager.tmx",                    32, 64, new boolean[] { true, true, true, true, true, true}),

 //   TRICKY_DROPS                     ("maps/Tricky_Drops.tmx",                     32, 64, new boolean[] { true, true, true, true, true, true}),
 // THIS_SHIT_DONT_WORK              ("maps/This_Shit_Dont_Work.tmx",              32, 64, new boolean[] { true, true, true, true, true, true}),


    END_CREDITS                      ("maps/End_Credits.tmx",   32, 64, new boolean[] { true, true, true, true, true, true});


    public String mapName;
    public float startX;
    public float startY;
    public boolean[] uiButtonStates;

    Level(String mapName, float startX, float startY, boolean[] uiButtonStates) {
        this.mapName = mapName;
        this.startX = startX;
        this.startY = startY;
        this.uiButtonStates = uiButtonStates;
    }

    public Vector2 getStart() {
        return new Vector2(startX, startY);
    }

}
