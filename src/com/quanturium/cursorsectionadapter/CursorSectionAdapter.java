package com.quanturium.cursorsectionadapter;

import java.util.SortedMap;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public abstract class CursorSectionAdapter extends CursorAdapter
{
	private Context						context;
	private SortedMap<Integer, Object>	sections;

	public CursorSectionAdapter(Context context, Cursor c)
	{
		super(context, c, 0);
		this.context = context;
		this.sections = initializeSections(c);

	}

	protected abstract SortedMap<Integer, Object> initializeSections(Cursor c);

	protected abstract boolean isSection(int position);

	public SortedMap<Integer, Object> getSections()
	{
		return sections;
	}

	protected int getRealItemPosition(int position)
	{
		int offset = 0;
		for (Integer k : sections.keySet())
		{
			if (position >= k)
				offset++;
			else
				break;
		}

		return (position - offset);
	}

	@Override
	public Object getItem(int position)
	{
		if (isSection(position))
			return this.sections.get(position);
		else
			return super.getItem(getRealItemPosition(position));
	}

	@Override
	public long getItemId(int position)
	{
		if (isSection(position))
			return position;
		else
			return super.getItemId(getRealItemPosition(position));
	}

	@Override
	public int getCount()
	{
		return super.getCount() + sections.size();
	}

	@Override
	public int getViewTypeCount()
	{
		return 2;
	}

	@Override
	public int getItemViewType(int position)
	{
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (isSection(position))
		{
			View v;
			if (convertView == null)
			{
				v = newSeparatorView(context, getItem(position), parent);
			}
			else
			{
				v = convertView;
			}
			bindSeparatorView(v, context, getItem(position));
			return v;
		}
		else
		{
			if (!getCursor().moveToPosition(getRealItemPosition(position)))
				throw new IllegalStateException("couldn't move cursor to position " + position);
			
			View v;
			if (convertView == null)
			{
				v = newView(context, getCursor(), parent);
			}
			else
			{
				v = convertView;
			}
			bindView(v, context, getCursor());
			return v;
		}
	}

	protected abstract void bindSeparatorView(View v, Context context2, Object item);

	protected abstract View newSeparatorView(Context context2, Object item, ViewGroup parent);
}
