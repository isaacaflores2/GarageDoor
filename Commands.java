/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garagedooropener;

/**
 *
 * @author iflores
 */

public enum Commands {
        TOGGLE (0), READ (1);
        
        int value; 
        
        Commands(int v){
            value = v;
        }
        
        int value(){return value; }
        
}

