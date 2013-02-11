package com.quanturium.cursorsectionadapter;

import java.util.SortedMap;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public abstract class CursorSectionAdapter extends CursorAdapter
{
	private Context						context;
	private SortedMap<Integer, Object>	sections;
	private DataSetObserver				dataSetObserver;

	public CursorSectionAdapter(Context context, Cursor c)
	{
		super(context, c, 0);
		this.context = context;
		
		if(c != null)
		{
			this.sections = initializeSections(c);
			this.dataSetObserver = new DataSetObserver()
			{
				@Override
				public void onChanged()
				{
					sections = initializeSections(getCursor());
				}
				
				@Override
				public void onInvalidated()
				{
					sections.clear();
				}
			};
			c.registerDataSetObserver(dataSetObserver);
		}		
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

	@Override
	public Cursor swapCursor(Cursor newCursor)
	{
		if (getCursor() != null)
		{
			getCursor().unregisterDataSetObserver(dataSetObserver);
		}

		Cursor oldCursor = super.swapCursor(newCursor);

		if (newCursor != null)
		{
			this.sections = initializeSections(newCursor);
			newCursor.registerDataSetObserver(dataSetObserver);
		}

		return oldCursor;
	}

	protected abstract void bindSeparatorView(View v, Context context2, Object item);

	protected abstract View newSeparatorView(Context context2, Object item, ViewGroup parent);
}
