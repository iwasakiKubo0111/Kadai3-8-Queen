import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

//途中作成コード 未完
//共通:Xの値は右へ行くほど高く Yの値は下へ行くほど高くなる
public class MyFrame extends JFrame implements MouseListener{
	private int px, py; //円を描画する座標(中心座標)
	private int XnumMap=8,YnumMap=8;//横縦コマの数
	private int win_x=820,win_y=850;//画面の大きさ
	private int box_w=100,box_h=100;//コマの大きさ
	private int QueenLeft=8;//置かなくちゃいけないQueenの残り数
	private int xy_map[][][]=new int[XnumMap][YnumMap][2];//●を描画する際の座標を格納[0]でY [1]でX
	private boolean IsQ_map[][]=new boolean[XnumMap][YnumMap];//Qが置かれているか 初期値はfalse
	public MyFrame(){ //コンストラクタ
	 setTitle("円の移動"); //ウィンドウタイトルを変更
	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 //↑ｳｨﾝﾄﾞｳを閉じたら終了する
	 setSize(win_x, win_y); //ウィンドウサイズを設定
	 px = 200; //円を描画する座標を(200,150)に設定
	 py = 150;
	 getContentPane().addMouseListener(this);
	}

	public void paint(Graphics g){ //再描画時の処理(paintのｵｰﾊﾞﾗｲﾄﾞ)
	 super.paint(g); //オーバライド前の処理を実行※11
	 g.translate(10, 40);

	 //マスの描画
	 for(int i=0;i<YnumMap;i++) {//縦描写
		 for(int j=0;j<XnumMap;j++) {//横へ描写
			 g.drawRect(box_w*i,box_h*j, box_w, box_h);//マスの描画
			 if(IsQ_map[i][j]==true) {
				 g.fillOval(box_w*j+10,box_h*i+10,80,80);//●描画
			 }
			 xy_map[i][j][1]=box_w*j+50;//横座標の格納 +50は都合を合わせるための値
			 xy_map[i][j][0]=box_h*i+50;//縦座標の格納
			 //System.out.print("Y:"+xy_map[i][j][0]);
			 //System.out.println(" X:"+xy_map[i][j][1]);
		 }
	 }
	 g = getContentPane().getGraphics(); //ｳｨﾝﾄﾞｳ内への描画用ｵﾌﾞｼﾞｪｸﾄ取得
	 g.fillOval(px-30, py-30, 60, 60); //(px, py)が中心になるように円を描画
	}

	public void mouseClicked(MouseEvent e){} //ﾏｳｽﾎﾞﾀﾝがクリックされたとき
	public void mouseEntered(MouseEvent e){} //ﾏｳｽがウィンドウ内に入ったとき
	public void mouseExited(MouseEvent e){} //ﾏｳｽがウィンドウ内から出たとき
	public void mousePressed(MouseEvent e){ //ﾏｳｽﾎﾞﾀﾝが押されたとき
	long start = System.nanoTime();
	 px = e.getX();
	 py =e.getY();
	System.out.print("Y:"+py);
	System.out.println(" X:"+px);
	//どこのマスが選択されたのか判定
	int t[]=WhereClickedMap(px,py);
	IsPutQueen(t[0],t[1]);
	long end = System.nanoTime();
    System.out.println((end - start) / 1000000f + "ms");
	 repaint();
	 }
	public void mouseReleased(MouseEvent e){
		Graphics g=getContentPane().getGraphics();
		g.drawRect(e.getX()-32,e.getY()-32,64,64);
	}

	//クリックされた座標を参照して選択された座標の中心を返す
	public int[] WhereClickedMap(int x,int y) {
		int t_yx[]=new int[2];//返却用配列
		int finalXP=9999,finalYP=9999;
		int finalX=0,finalY=0;

		for(int i=0;i<YnumMap;i++) {//まず縦から
			//System.out.println(Math.abs(y-xy_map[i][0][0]));
			if(i>0) {
				if(Math.abs(y-xy_map[i][0][0])>finalYP) {
					break;//これ以降は無駄判定になるので脱出
				}
			}
			if(Math.abs(y-xy_map[i][0][0])<finalYP) {
				finalYP=Math.abs(y-xy_map[i][0][0]);
				finalY=i;
			}
		}
		//横判定
		for(int j=0;j<XnumMap;j++) {
			if(j>0) {
				if(Math.abs(x-xy_map[0][j][1])>finalXP) {
					break;//これ以降は無駄判定になるので脱出
				}
			}
			if(Math.abs(x-xy_map[0][j][1])<finalXP) {
				finalXP=Math.abs(x-xy_map[0][j][1]);
				finalX=j;
			}
		}
		//最終的な値を代入 返却
		t_yx[0]=finalY;
		t_yx[1]=finalX;
		System.out.print("Y:"+t_yx[0]);
		System.out.println(" X:"+t_yx[1]);
		return t_yx;

	}

	public boolean IsPutQueen(int x,int y) {//置けるかどうかの判定 置けるなら置く?
		if(IsQ_map[x][y]==true)return false;//そもそも置いてあるから置けない。
		IsQ_map[x][y]=true;//とりあえず置いてみる
		int direction[][]= {{0,1},{1,1},{1,0},{1,-1},{0,-1},{-1,-1},{-1,0},{-1,1}};//{xの上昇量,yの上昇量}
		//System.out.println(direction[7][0]);//-1
		for(int i=0;i<7;i++) {//7はとりあえず仮
			for(int j=0;j<7;j++) {
				if(IsQ_map[x+direction[i][0]*j][y+direction[i][1]*j]==true)return false;//多分既に置かれてる所には置かない判定(うろ覚え)
			}
		}


		return true;
		/*if(IsQ_map[i][j]==true) {
			 g.fillOval(box_w*j+10,box_h*i+10,80,80);//●描画
		 }*/
	}

	}


