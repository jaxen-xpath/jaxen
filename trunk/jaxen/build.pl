#!/usr/bin/perl

use strict;
use Cwd;

my %options;
my $true;
my $false;

my $delim;

my $java_home;
my $classpath;
my @tools_classpath;
my @lib_classpath;

$true  = 1;
$false = 0;

if ( $^O =~ /win32/i ) 
{
   $delim = ";";
}
else
{
   $delim = ":";
}

$| = 1;

## ------------------------------------------------------------------------
## ------------------------------------------------------------------------

if ( &checkJavaHome() ) 
{
	exit( 1 );
}

&setupJava();
&dumpBuildInfo();
&execAnt();

## ------------------------------------------------------------------------
## ------------------------------------------------------------------------

sub dumpBuildInfo()
{
	print "-----------------------------------------------\n\n";


	my $sec;
	my $min;
	my $hour;
	my $mday;
	my $mon;
	my $year;
	my $wday;
	my $yday;
	my $isdst;

	($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime(time);

	$year = $year + 1900;

	my $weekday = ("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")[$wday];
	my $month   = ("January","February","March","April","May","June","July","August","September","October","November","December")[$mon];


	print "      date: $hour:$min:$sec $weekday $mday-$month-$year\n"; 
	print " directory: ", cwd(), "\n";

	my $targets;
	
	if ( $#ARGV >= 0 ) 
	{
		$targets = join ', ',  @ARGV;
	}
	else
	{
		$targets = '*default*';
	}
	print "   targets: $targets\n\n";

	print "-- CLASSPATH ----------------------------------\n";

	my $element;
	foreach $element ( split $delim, $classpath )
	{
		print "    $element\n";
	}

	print "-----------------------------------------------\n";
}

sub execAnt()
{
	system "$java_home/bin/java -cp $classpath org.apache.tools.ant.Main @ARGV";
}

sub setupJava()
{
	$java_home       = $ENV{"JAVA_HOME"};
	@tools_classpath = "$java_home/lib/tools.jar";

	@lib_classpath   = glob("./lib/*.jar");

	my $system_class_path;

	my @keys;

	$system_class_path = $ENV{"CLASSPATH"};

	$classpath = join $delim, @tools_classpath, @lib_classpath, $system_class_path;

	$ENV{"CLASSPATH"} = $classpath;
}


sub checkJavaHome()
{
	if ( $ENV{"JAVA_HOME"} eq "" )
	{
		print "ERROR: JAVA_HOME not found in your environment.\n\n";
		print "Please, set the JAVA_HOME variable in your environment\n";
		print "to match the location of the Java Virtual Machine\n";
		print "that you want to use.\n\n";
	
		return 1;
	}

	$java_home = $ENV{"JAVA_HOME"};
	return 0;
}
