/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topIx;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

/**
 *
 * @author Antiregulator
 */
public class DynamicTree extends JPanel {

    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree roomsTree;
    //private Toolkit toolkit=Toolkit.getDefaultToolkit();

    public DynamicTree() {
        super(new GridLayout(1, 0));

        //rootNode = new DefaultMutableTreeNode("Inserted Rooms");
        //treeModel = new DefaultTreeModel(rootNode);
        roomsTree = new JTree(/*treeModel*/);
        roomsTree.setEditable(false);
        roomsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane treeScroller = new JScrollPane(roomsTree);
        this.add(treeScroller);
    }

    public DynamicTree(String SiteName) {
        super(new GridLayout(1, 0));

        //rootNode = new DefaultMutableTreeNode(SiteName);
        //treeModel = new DefaultTreeModel(rootNode);

//        roomsTree = new JTree();
//        roomsTree.setEditable(false);
//        roomsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane treeScroller = new JScrollPane(roomsTree);
        this.add(treeScroller);
    }
    
    public JTree getTree () {
        return roomsTree;
    }

    public void clearTree() {
        rootNode.removeAllChildren();
        treeModel.reload();
    }
    
    public void addRootNodeToTree(String rootNode) {
        treeModel=new DefaultTreeModel(new DefaultMutableTreeNode(rootNode));
        roomsTree.setModel(treeModel);
        roomsTree.setEditable(false);
        roomsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
    }

    public void addHouseNodeToTree(String houseStr) {
        DefaultMutableTreeNode houseChildNode = new DefaultMutableTreeNode(houseStr);
        DefaultMutableTreeNode tempRootNode=(DefaultMutableTreeNode)treeModel.getRoot();
        treeModel.insertNodeInto(houseChildNode, tempRootNode, tempRootNode.getChildCount());
        roomsTree.expandPath(roomsTree.getPathForRow(0));
    }

    public void addRoomNodeToTree(String roomStr/*, MutableTreeNode or String*/) {
        DefaultMutableTreeNode roomChildNode = new DefaultMutableTreeNode(roomStr);
        DefaultMutableTreeNode tempParentNode=(DefaultMutableTreeNode)roomsTree.getLastSelectedPathComponent();
        treeModel.insertNodeInto(roomChildNode, tempParentNode, tempParentNode.getChildCount());
        //edw tha prostethei me thn insertNodeInto to neo room me parametroys:
        //i.ton pros dhmioyrgia komvo se morfh MutableTreeNode (roomChildNode)
        //ii.thn parametro toy epilegmenoy house node poy eite tha mpainei sth method ws string
        //   eite tha mpainei ws MutableTreeNode, analoga ti epistrefei to DefaultTreeModel
        //   h opoia allh klash einai ypeythynh na epistrefei me kapoia morfh ton epilegmeno komvo.
    }
    
    public String returnSelectedNodeString() {
        DefaultMutableTreeNode returnSelectedNode=(DefaultMutableTreeNode)roomsTree.getLastSelectedPathComponent();
        return returnSelectedNode.toString();
    }
}