package com.zifang.demo.designpatten;

/**
 * 适配器模式
 * 	适配器模式（Adapter Pattern）是作为两个不兼容的接口之间的桥梁。这种类型的设计模式属于结构型模式，它结合了两个独立接口的功能。
 * 	这种模式涉及到一个单一的类，该类负责加入独立的或不兼容的接口功能。举个真实的例子，读卡器是作为内存卡和笔记本之间的适配器。
 * 		您将内存卡插入读卡器，再将读卡器插入笔记本，这样就可以通过笔记本来读取内存卡。
 * 	我们通过下面的实例来演示适配器模式的使用。其中，音频播放器设备只能播放 mp3 文件，通过使用一个更高级的音频播放器来播放 vlc 和 mp4 文件。
 * 介绍
 * 	意图：将一个类的接口转换成客户希望的另外一个接口。适配器模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。
 * 	主要解决：主要解决在软件系统中，常常要将一些"现存的对象"放到新的环境中，而新环境要求的接口是现对象不能满足的。
 * 	何时使用： 
 * 		1、系统需要使用现有的类，而此类的接口不符合系统的需要。 
 * 		2、想要建立一个可以重复使用的类，用于与一些彼此之间没有太大关联的一些类，包括一些可能在将来引进的类一起工作，这些源类不一定有一致的接口。 
 * 		3、通过接口转换，将一个类插入另一个类系中。（比如老虎和飞禽，现在多了一个飞虎，在不增加实体的需求下，增加一个适配器，
 * 		在里面包容一个虎对象，实现飞的接口。）
 * 	如何解决：继承或依赖（推荐）。
 * 	关键代码：适配器继承或依赖已有的对象，实现想要的目标接口。
 * 	应用实例： 
 * 		1、美国电器 110V，中国 220V，就要有一个适配器将 110V 转化为 220V。
 * 		2、JAVA JDK 1.1 提供了 Enumeration 接口，而在 1.2 中提供了 Iterator 接口，想要使用 1.2 的 JDK，
 * 			则要将以前系统的 Enumeration 接口转化为 Iterator 接口，这时就需要适配器模式。 
 * 		3、在 LINUX 上运行 WINDOWS 程序。 4、JAVA 中的 jdbc。
 * 	优点： 
 * 		1、可以让任何两个没有关联的类一起运行。 
 * 		2、提高了类的复用。 
 * 		3、增加了类的透明度。 
 * 		4、灵活性好。
 * 	缺点： 
 * 		1、过多地使用适配器，会让系统非常零乱，不易整体进行把握。比如，明明看到调用的是 A 接口，其实内部被适配成了 B 接口的实现，一个系统如果太多出现这种情况，无异于一场灾难。
 * 			因此如果不是很有必要，可以不使用适配器，而是直接对系统进行重构。 
 * 		2.由于 JAVA 至多继承一个类，所以至多只能适配一个适配者类，而且目标类必须是抽象类。
 * 	使用场景：有动机地修改一个正常运行的系统的接口，这时应该考虑使用适配器模式。
 * 	注意事项：适配器不是在详细设计时添加的，而是解决正在服役的项目的问题。
 *
 *
 */
public class AdapterPatternDemo {

	/**
	 * 步骤 1
	 * 
	 * 为媒体播放器和更高级的媒体播放器创建接口。
	 * 
	 *
	 *
	 */
	public interface MediaPlayer {
		public void play(String audioType, String fileName);
	}

	public interface AdvancedMediaPlayer {
		public void playVlc(String fileName);

		public void playMp4(String fileName);
	}

	/**
	 * 步骤 2
	 * 
	 * 创建实现了 AdvancedMediaPlayer 接口的实体类。
	 * 
	 *
	 *
	 */
	static class VlcPlayer implements AdvancedMediaPlayer {
		@Override
		public void playVlc(String fileName) {
			System.out.println("Playing vlc file. Name: " + fileName);
		}

		@Override
		public void playMp4(String fileName) {
			// 什么也不做
		}
	}

	static class Mp4Player implements AdvancedMediaPlayer {

		@Override
		public void playVlc(String fileName) {
			// 什么也不做
		}

		@Override
		public void playMp4(String fileName) {
			System.out.println("Playing mp4 file. Name: " + fileName);
		}
	}

	/**
	 * 步骤 3
	 * 
	 * 创建实现了 MediaPlayer 接口的适配器类。
	 * 
	 *
	 *
	 */
	static class MediaAdapter implements MediaPlayer {

		AdvancedMediaPlayer advancedMusicPlayer;

		public MediaAdapter(String audioType) {
			if (audioType.equalsIgnoreCase("vlc")) {
				advancedMusicPlayer = new VlcPlayer();
			} else if (audioType.equalsIgnoreCase("mp4")) {
				advancedMusicPlayer = new Mp4Player();
			}
		}

		@Override
		public void play(String audioType, String fileName) {
			if (audioType.equalsIgnoreCase("vlc")) {
				advancedMusicPlayer.playVlc(fileName);
			} else if (audioType.equalsIgnoreCase("mp4")) {
				advancedMusicPlayer.playMp4(fileName);
			}
		}
	}

	/**
	 * 步骤 4
	 * 
	 * 创建实现了 MediaPlayer 接口的实体类。
	 * 
	 *
	 *
	 */
	static class AudioPlayer implements MediaPlayer {
		MediaAdapter mediaAdapter;

		@Override
		public void play(String audioType, String fileName) {

			// 播放 mp3 音乐文件的内置支持
			if (audioType.equalsIgnoreCase("mp3")) {
				System.out.println("Playing mp3 file. Name: " + fileName);
			}
			// mediaAdapter 提供了播放其他文件格式的支持
			else if (audioType.equalsIgnoreCase("vlc") || audioType.equalsIgnoreCase("mp4")) {
				mediaAdapter = new MediaAdapter(audioType);
				mediaAdapter.play(audioType, fileName);
			} else {
				System.out.println("Invalid media. " + audioType + " format not supported");
			}
		}
	}

	/**
	 * 步骤 5
	 * 
	 * 使用 AudioPlayer 来播放不同类型的音频格式。
	 */
	public static void main(String[] args) {
		AudioPlayer audioPlayer = new AudioPlayer();

		audioPlayer.play("mp3", "beyond the horizon.mp3");
		audioPlayer.play("mp4", "alone.mp4");
		audioPlayer.play("vlc", "far far away.vlc");
		audioPlayer.play("avi", "mind me.avi");
	}
}
