version
=======

version


1、swing不能直接对gui组建编写代码，要把任务提交给SwingUtilities.invokeLater()来执行。
  如：SwingUtilities.invokeLater(new Runnable(){public void run(){PlatFormFrame frame = FrameFactory.getPlatFormFrame()}});